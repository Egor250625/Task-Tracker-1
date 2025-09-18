package com.tasktracker.schedulerservice.kafka;

import com.tasktracker.schedulerservice.dto.request.TaskNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskNotificationProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.task-notification}")
    private String topic;

    public void send(TaskNotificationDto dto) {
        log.info("TaskNotificationDto: Sending message to consumer with parameters  = {}", dto.toString());
        kafkaTemplate.send(topic, dto);
        log.info("TaskNotificationDto: Successfully send to topic  = {}", topic);
    }
}
