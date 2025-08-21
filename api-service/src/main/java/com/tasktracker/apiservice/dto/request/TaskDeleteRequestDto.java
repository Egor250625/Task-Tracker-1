package com.tasktracker.apiservice.dto.request;

import jakarta.validation.constraints.NotNull;

public record TaskDeleteRequestDto(@NotNull int id){
}
