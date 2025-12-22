package com.urbannest.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String fullName, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("noreply@urbannest.com");
        helper.setTo(to);
        helper.setSubject("Account Verification - UrbanNest");
        
        String htmlContent = String.format(
            "<html>" +
            "<body>" +
            "<h2>Welcome to UrbanNest, %s!</h2>" +
            "<p>Thank you for registering. Please use the following code to verify your account:</p>" +
            "<h1 style='color: #4F46E5; letter-spacing: 5px;'>%s</h1>" +
            "<p>This code will expire in 15 minutes.</p>" +
            "</body>" +
            "</html>",
            fullName, code
        );

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
