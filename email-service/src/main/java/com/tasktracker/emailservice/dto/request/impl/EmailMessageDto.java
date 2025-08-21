package com.tasktracker.emailservice.dto.request.impl;

import com.tasktracker.emailservice.dto.request.EmailSendable;

import java.util.Map;

public record EmailMessageDto(String to, String type, Map<String, String> params) implements EmailSendable {
}
