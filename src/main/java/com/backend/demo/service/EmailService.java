package com.backend.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private final JavaMailSender mailSender;

    // Constructor injection
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("arpan.mufasa564@gmail.com"); // Set your sender email here
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (MailException e) {
            System.err.println("Error sending email to " + to + ": " + e.getMessage());
            // Log the error for debugging, but don't rethrow if email failure shouldn't
            // stop the main cancellation process. Consider using a dedicated logging framework.
        }
    }
}