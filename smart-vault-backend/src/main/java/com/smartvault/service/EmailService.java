package com.smartvault.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendWelcomeEmail(String to, String name) {
        String subject = "Welcome to Smart Vault!";
        String htmlBody = "<h1>Welcome, " + name + "!</h1>" +
                "<p>Thank you for joining Smart Vault. Securely store and manage your important documents and links.</p>";
        sendHtmlEmail(to, subject, htmlBody);
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        String subject = "Password Reset Request - Smart Vault";
        String htmlBody = "<h1>Password Reset</h1>" +
                "<p>Click the link below to reset your password. This link will expire in 1 hour.</p>" +
                "<a href='" + resetLink + "'>Reset Password</a>";
        sendHtmlEmail(to, subject, htmlBody);
    }

    public void sendExpiryReminder(String to, String name, String documentTitle, LocalDate expiryDate) {
        String subject = "Document Expiry Reminder - " + documentTitle;
        String htmlBody = "<h1>Document Expiring Soon</h1>" +
                "<p>Hello " + name + ",</p>" +
                "<p>Your document <strong>" + documentTitle + "</strong> is expiring on <strong>" + expiryDate + "</strong>.</p>" +
                "<p>Please log in to Smart Vault to renew or update it.</p>";
        sendHtmlEmail(to, subject, htmlBody);
    }

    public void sendShareNotification(String to, String name, String documentTitle, String shareLink) {
        String subject = "Document Shared With You";
        String htmlBody = "<h1>Document Shared</h1>" +
                "<p>Hello " + name + ",</p>" +
                "<p>A document titled <strong>" + documentTitle + "</strong> has been shared. You can access it using the link below:</p>" +
                "<a href='" + shareLink + "'>View Document</a>";
        sendHtmlEmail(to, subject, htmlBody);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}", to, e);
        }
    }
}
