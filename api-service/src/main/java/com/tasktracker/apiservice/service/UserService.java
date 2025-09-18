package com.tasktracker.apiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasktracker.apiservice.dto.request.EmailMessageRequestDto;
import com.tasktracker.apiservice.dto.request.UserCreateEditDto;
import com.tasktracker.apiservice.dto.response.UserReadDto;
import com.tasktracker.apiservice.exception.EmailAlreadyExistException;
import com.tasktracker.apiservice.kafka.producer.EmailKafkaProducer;
import com.tasktracker.apiservice.mapper.UserMapper;
import com.tasktracker.apiservice.model.entity.User;
import com.tasktracker.apiservice.model.entity.UserEvent;
import com.tasktracker.apiservice.repository.UserEventRepository;
import com.tasktracker.apiservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final EmailKafkaProducer emailKafkaProducer;

    private final String WELCOME_EMAIL_TYPE = "WELCOME";

    private final UserEventRepository userEventRepository;

    private final ObjectMapper objectMapper;


    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) {
        return Optional.of(userDto)
                .filter(dto -> !userRepository.existsByEmail(dto.email()))
                .map(dto -> {
                    User user = userMapper.toEntity(dto);
                    user.setPassword(passwordEncoder.encode(dto.password()));
                    log.info("Password encode correct for user {}", dto.email());
                    User savedUser = userRepository.save(user);
                    userEventRepository.save(buildUserEvent(savedUser.getEmail()));
                    log.info("Event save to database,with email {}",savedUser.getEmail());
                    return savedUser;
                })
                .map(userMapper::toDto)
                .orElseThrow(() -> new EmailAlreadyExistException(
                        "Account with this email - {}, already exists.Or it is incorrect", userDto.email()
                ));
    }

    private UserEvent buildUserEvent(String email) {
        return UserEvent.builder()
                .pullCount(0)
                .enqueuedAt(LocalDateTime.now())
                .reservedTo(LocalDateTime.now().plusSeconds(5)) // небольшая задержка
                .message(objectMapper.valueToTree(
                        new EmailMessageRequestDto(
                                email,
                                WELCOME_EMAIL_TYPE,
                                new HashMap<>()
                        )
                ))
                .build();
    }
}
