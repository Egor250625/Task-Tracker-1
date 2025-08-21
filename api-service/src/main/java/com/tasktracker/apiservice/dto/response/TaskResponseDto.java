package com.tasktracker.apiservice.dto.response;

import com.tasktracker.apiservice.model.Status;

import java.time.LocalDateTime;

public record TaskResponseDto(
        int id,
        String title,
        String description,
        Status status,
        LocalDateTime completedAt) {
}
