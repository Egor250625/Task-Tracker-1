package com.tasktracker.apiservice.kafka.producer;

import com.tasktracker.apiservice.dto.request.EmailMessageRequestDto;
import com.tasktracker.apiservice.dto.request.UserInformationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskNotificationKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.user-info}")
    private String topic;

    public void send(UserInformationDto dto){
        log.info("UserInformationDto: Sending message to consumer = {}", dto);
        kafkaTemplate.send(topic, dto);
    }
}
