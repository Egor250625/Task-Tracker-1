package com.tasktracker.emailservice.service;

import com.tasktracker.emailservice.dto.request.EmailSendable;
import com.tasktracker.emailservice.dto.request.impl.EmailMessageDto;
import com.tasktracker.emailservice.handler.EmailHandler;
import com.tasktracker.emailservice.model.EmailType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final Map<String, EmailHandler> emailHandlers;

    private final Map<EmailType, EmailHandler> handlers = new EnumMap<>(EmailType.class);


    @PostConstruct
    private void initHandlers() {
        emailHandlers.forEach((key, handler) -> {
            EmailType type = EmailType.valueOf(key);
            log.info("Message type set  = {}", type.name());
            handlers.put(type, handler);
        });
    }


    public void sendEmail(EmailSendable dto) {
        if (dto == null || dto.to() == null || !dto.to().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid recipient email");
        }
        EmailType type;
        try {
            type = EmailType.valueOf(dto.type().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown email type: " + dto.type());
        }
        var handler = handlers.get(type);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for type: " + type);
        }
        handler.handle(dto);
        log.info("Message correctly send with type {},to email {}", dto.type(), dto.to());
    }

}
