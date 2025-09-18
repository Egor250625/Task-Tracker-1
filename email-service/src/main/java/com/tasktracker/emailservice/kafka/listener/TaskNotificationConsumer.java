package com.tasktracker.emailservice.kafka.listener;

import com.tasktracker.emailservice.dto.request.impl.TaskNotificationDto;
import com.tasktracker.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskNotificationConsumer {
    private final EmailService emailService;

    @KafkaListener(
            topics = "${kafka.topics.task-notification}",
            groupId = "${spring.kafka.consumer.task-group-id}",
            containerFactory = "taskNotificationKafkaListenerContainerFactory"
    )
    public void consume(TaskNotificationDto dto) {
        log.info("Received task = {}", dto);
        emailService.sendEmail(dto);
    }
}
