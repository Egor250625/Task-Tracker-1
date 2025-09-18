package com.tasktracker.apiservice.dto.request;

import com.tasktracker.apiservice.model.Status;
import jakarta.validation.constraints.NotBlank;

public record TaskCreateRequestDto(
        @NotBlank String title,
        String description,
        Status status) {
}
