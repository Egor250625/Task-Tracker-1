package com.tasktracker.apiservice.dto.request;

import java.util.Map;

public record EmailMessageRequestDto(String to, String type, Map<String, String> params) {
}
