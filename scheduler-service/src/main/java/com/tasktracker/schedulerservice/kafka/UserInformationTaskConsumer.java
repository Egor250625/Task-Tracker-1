package com.tasktracker.schedulerservice.kafka;

import com.tasktracker.schedulerservice.dto.request.UserInformationDto;
import com.tasktracker.schedulerservice.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInformationTaskConsumer {
    private final SchedulerService schedulerService;

    @KafkaListener(topics = "${kafka.topics.user-info}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(UserInformationDto messageDto) {
        log.info("Get message = {},user Id  = {}", messageDto.toString(), messageDto.id());
        schedulerService.sendUserTasksSummary(messageDto.id());
    }
}
