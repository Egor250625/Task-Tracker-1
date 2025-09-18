package com.tasktracker.apiservice.dto.request;

import com.tasktracker.apiservice.model.Status;
import jakarta.validation.constraints.NotNull;

public record TaskUpdateRequestDto(@NotNull(message = "Task id must be specified.") int id,
                                   @NotNull(message = "The title must not be empty. ") String title,
                                   String description,
                                   Status status) {
}
