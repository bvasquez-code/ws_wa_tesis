package com.ccadmin.app.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    private static final String SMTP_HOST = "smtp.gmail.com";        // ej: smtp.sendgrid.net, email-smtp.us-east-1.amazonaws.com
    private static final int    SMTP_PORT = 587;                     // 587 (TLS) o 465 (SSL)
    private static final String SMTP_USER = "matematicajava365@gmail.com";   // ej: no-reply@tudominio.com
    private static final String SMTP_PASS = "1q2w3e4r@@@AACC56";// ej: app password de Gmail

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(SMTP_HOST);
        sender.setPort(SMTP_PORT);
        sender.setUsername(SMTP_USER);
        sender.setPassword(SMTP_PASS);
        sender.setDefaultEncoding("UTF-8");

        Properties props = sender.getJavaMailProperties();
        // TLS (587)
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Si usas SSL directo (465), comenta la línea anterior y descomenta esta:
        // props.put("mail.smtp.ssl.enable", "true");

        // (Opcional) timeouts
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");

        return sender;
    }
}
