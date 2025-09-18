package com.tasktracker.emailservice.handler.impl;

import com.tasktracker.emailservice.dto.request.EmailSendable;
import com.tasktracker.emailservice.dto.request.impl.EmailMessageDto;
import com.tasktracker.emailservice.handler.EmailHandler;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Component("WELCOME")
@RequiredArgsConstructor
public class WelcomeEmailHandler implements EmailHandler {

    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;


    @Value("${task-tracker.mail.from}")
    private String fromEmail;

    @Override
    public void handle(EmailSendable dto) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(dto.to());
            helper.setSubject("Welcome!");

            // Получаем ресурс и читаем через InputStream
            Resource resource = resourceLoader.getResource("classpath:templates/welcome-email.html");
            String htmlContent;
            try (InputStream is = resource.getInputStream()) {
                htmlContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}
