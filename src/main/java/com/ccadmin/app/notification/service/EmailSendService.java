package com.ccadmin.app.notification.service;

import com.ccadmin.app.notification.model.dto.EmailSendDto;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSendService {

    // Cambia este remitente fijo si quieres
    private static final String FROM = "Notificaciones <no-reply@tudominio.com>";

    @Autowired
    private JavaMailSender mailSender;

    public void send(EmailSendDto dto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(FROM);
            helper.setTo(dto.Email);
            helper.setSubject(dto.Subject);
            helper.setText(dto.Body, false); // true => HTML

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando correo: " + e.getMessage(), e);
        }
    }
}
