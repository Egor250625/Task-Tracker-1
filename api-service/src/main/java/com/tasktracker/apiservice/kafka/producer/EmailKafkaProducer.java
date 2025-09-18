package com.tasktracker.apiservice.kafka.producer;

import com.tasktracker.apiservice.dto.request.EmailMessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.email-sending}")
    private String topic;

    public void send(EmailMessageRequestDto dto) {
        log.info("EmailMessageRequestDto: Sending message to consumer = {}", dto);
        kafkaTemplate.send(topic, dto);
    }
}
