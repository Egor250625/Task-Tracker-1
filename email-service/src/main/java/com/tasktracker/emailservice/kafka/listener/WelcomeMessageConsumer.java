package com.tasktracker.emailservice.kafka.listener;

import com.tasktracker.emailservice.dto.request.impl.EmailMessageDto;
import com.tasktracker.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WelcomeMessageConsumer {

    private final EmailService emailService;


    @KafkaListener(
            topics = "${kafka.topics.email-sending}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "emailMessageKafkaListenerContainerFactory"
    )
    public void consume(EmailMessageDto messageDto) {
        log.info("Get message = {}", messageDto.toString());
        emailService.sendEmail(messageDto);
    }


}
