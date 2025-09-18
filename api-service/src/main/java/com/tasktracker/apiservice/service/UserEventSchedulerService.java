package com.tasktracker.apiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasktracker.apiservice.dto.request.EmailMessageRequestDto;
import com.tasktracker.apiservice.kafka.producer.EmailKafkaProducer;
import com.tasktracker.apiservice.model.entity.UserEvent;
import com.tasktracker.apiservice.repository.UserEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventSchedulerService {

    private final UserEventRepository userEventRepository;
    private final ObjectMapper objectMapper;
    private final EmailKafkaProducer emailKafkaProducer;

    private final int CHUNK_SIZE = 10;
    private final int DELAY_SECONDS = 30;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processUserEvents() {
        List<UserEvent> events = userEventRepository.fetchAndLockEvents(CHUNK_SIZE, DELAY_SECONDS);

        for (UserEvent userEvent : events) {
            try {
                EmailMessageRequestDto messageRequestDto = objectMapper.treeToValue(
                        userEvent.getMessage(),
                        EmailMessageRequestDto.class
                );
                emailKafkaProducer.send(messageRequestDto);
                userEventRepository.delete(userEvent);
            } catch (Exception e) {
                log.error("Failed to send user event id={}, will retry later", userEvent.getId(), e);
                userEvent.setReservedTo(LocalDateTime.now().plusSeconds(DELAY_SECONDS));
                userEventRepository.save(userEvent);
            }
        }
    }
}
