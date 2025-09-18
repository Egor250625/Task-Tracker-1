package com.tasktracker.emailservice.handler.impl;

import com.tasktracker.emailservice.dto.request.EmailSendable;
import com.tasktracker.emailservice.dto.request.impl.TaskNotificationDto;
import com.tasktracker.emailservice.handler.EmailHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.stream.Collectors;

@Component("TASK_REPORT")
@RequiredArgsConstructor
@Slf4j
public class TaskSummaryEmailHandler implements EmailHandler {
    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;
    private final SpringTemplateEngine templateEngine;


    @Value("${task-tracker.mail.from}")
    private String fromEmail;



    @Override
    public void handle(EmailSendable dto) {
        if (dto instanceof TaskNotificationDto taskDto) {
            List<String> completedTasks = taskDto.tasks().getOrDefault("COMPLETED", List.of());
            List<String> inProgressTasks = taskDto.tasks().getOrDefault("IN_PROGRESS", List.of());
            int completedCount = taskDto.completedCount();
            int pendingCount = taskDto.pendingCount();
            // дальше формируем html
            String completedTasksStr = completedTasks.stream().map(t -> "• " + t + "<br>").collect(Collectors.joining());
            String inProgressTasksStr = inProgressTasks.stream().map(t -> "• " + t + "<br>").collect(Collectors.joining());
            Context context = new Context();
            context.setVariable("completedCount", completedCount);
            context.setVariable("pendingCount", pendingCount);
            context.setVariable("completedTasks", completedTasksStr);
            context.setVariable("inProgressTasks", inProgressTasksStr);

            String htmlContent = templateEngine.process("task-summary.html", context);

            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setFrom(fromEmail);
                helper.setTo(dto.to());
                helper.setSubject("Daily Task Summary");
                helper.setText(htmlContent, true);

                mailSender.send(mimeMessage);

                log.info("Task summary email sent to {}", dto.to());

            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send email", e);
            }

        } else {
            throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass());
        }
    }

}
