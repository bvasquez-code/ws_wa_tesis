package com.ccadmin.app.schooldata.service;

import com.ccadmin.app.schooldata.model.entity.DataAutoProcessEntity;
import com.ccadmin.app.schooldata.model.entity.DataAutoProcessExecEntity;
import com.ccadmin.app.schooldata.repository.DataAutoProcessExecRepository;
import com.ccadmin.app.schooldata.repository.DataAutoProcessRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
public class AutoProcessSchedulerService {

    @Autowired
    private DataAutoProcessRepository processRepository;

    @Autowired
    private DataAutoProcessExecRepository execRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private ZoneId zone = ZoneId.systemDefault();

    @PostConstruct
    public void init() {
        // this.zone = ZoneId.of("America/Lima");
    }

    /**
     * FRECUENCIA DEL SCAN:
     *  - PRUEBAS (cada minuto):  "0 * * * * *"
     *  - PRODUCCIÓN (cada hora): "0 0 * * * *"
     */
    @Scheduled(cron = "0 0 * * * *") // ← cámbialo a "0 0 * * * *" para cada hora
    public void scanAndRunWindow() {
        ZonedDateTime now = ZonedDateTime.now(zone);
        ZonedDateTime windowEnd = now.plusHours(1);

        List<DataAutoProcessEntity> actives = this.processRepository.findAllActive();

        for (var p : actives) {
            try {
                if (shouldRunInWindow(p, now, windowEnd)) {
                    // Lanzar en paralelo con HILO VIRTUAL nativo de Java 21
                    Thread.startVirtualThread(() -> {
                        try {
                            executeProcess(p, "A"); // A = Automático
                        } catch (Exception ignored) {}
                    });
                }
            } catch (Exception ignored) {
                // No detener el loop por errores de un proceso
            }
        }
    }

    public DataAutoProcessExecEntity executeProcess(DataAutoProcessEntity p, String triggeredBy) {
        DataAutoProcessExecEntity exec = new DataAutoProcessExecEntity();
        exec.ProcessCode = p.ProcessCode;
        exec.TriggeredBy = triggeredBy;
        exec.StartDate = new Date();
        exec.RunStatus = "R";
        exec.Hostname = getHostnameSafe();
        exec = this.execRepository.save(exec); // save inicial (estado: Running)

        long t0 = System.currentTimeMillis();
        try {
            String url = resolveUrl(p.ProcessCode);
            var response = this.restTemplate.postForEntity(url, null, String.class);

            long t1 = System.currentTimeMillis();
            exec.EndDate = new Date();
            exec.DurationMs = (t1 - t0);
            exec.RunStatus = response.getStatusCode().is2xxSuccessful() ? "S" : "E";
            exec.Message = "HTTP " + response.getStatusCodeValue()
                    + (response.getBody() != null ? " | " + safeTrim(response.getBody(), 450) : "");
        } catch (Exception ex) {
            long t1 = System.currentTimeMillis();
            exec.EndDate = new Date();
            exec.DurationMs = (t1 - t0);
            exec.RunStatus = "E";
            exec.Message = safeTrim(ex.getMessage(), 490);
            exec.ErrorStack = getStackTraceAsString(ex);
        }
        return this.execRepository.save(exec); // save final (estado: S/E)
    }

    private boolean shouldRunInWindow(DataAutoProcessEntity p, ZonedDateTime now, ZonedDateTime windowEnd) {
        // Evitar duplicidad si ya se ejecutó dentro de la ventana
        if (execRepository.countExecutionsInWindow(
                p.ProcessCode,
                Date.from(now.toInstant()),
                Date.from(windowEnd.toInstant())) > 0) {
            return false;
        }

        // 1) CronExpr
        if (p.CronExpr != null && !p.CronExpr.trim().isEmpty()) {
            try {
                CronExpression cron = CronExpression.parse(p.CronExpr);
                ZonedDateTime next = cron.next(now.minusSeconds(1)); // por si toca "ahora"
                return next != null && !next.isAfter(windowEnd);
            } catch (Exception ignore) {
                // si cron inválido, cae a IntervalMin
            }
        }

        // 2) IntervalMin
        if (p.IntervalMin != null && p.IntervalMin > 0) {
            var last = execRepository.findLastExec(p.ProcessCode);
            ZonedDateTime base = (last == null || last.StartDate == null)
                    ? now
                    : ZonedDateTime.ofInstant(last.StartDate.toInstant(), zone);
            ZonedDateTime next = base.plusMinutes(p.IntervalMin);
            return !next.isAfter(windowEnd);
        }

        return false;
    }

    private String resolveUrl(String processCode) {
        switch (processCode) {
            case "RETRAIN_DIAG_MODEL":
                return "https://wswaexam-production.up.railway.app/train_diagnostic_model";
            case "RETRAIN_RANK_MODEL":
                return "https://wswaexam-production.up.railway.app/generate_rank_exam/train";
            default:
                throw new IllegalArgumentException("URL no definida para " + processCode);
        }
    }

    private String getHostnameSafe() {
        try { return InetAddress.getLocalHost().getHostName(); }
        catch (Exception e) { return "unknown-host"; }
    }

    private String getStackTraceAsString(Throwable t) {
        try (java.io.StringWriter sw = new java.io.StringWriter();
             java.io.PrintWriter pw = new java.io.PrintWriter(sw)) {
            t.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            return t.toString();
        }
    }

    private String safeTrim(String s, int maxLen) {
        if (s == null) return null;
        return (s.length() <= maxLen) ? s : s.substring(0, maxLen);
    }
}
