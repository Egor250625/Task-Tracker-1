package com.tasktracker.apiservice.dto.response;

public record ApiErrorResponse(
        String message,
        int status,
        String path
) {}