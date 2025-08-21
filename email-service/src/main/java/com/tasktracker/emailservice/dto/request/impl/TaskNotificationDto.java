package com.tasktracker.emailservice.dto.request.impl;

import com.tasktracker.emailservice.dto.request.EmailSendable;

import java.util.List;
import java.util.Map;

public record TaskNotificationDto(String to,
                                  String type,
                                  Map<String, List<String>> tasks,
                                  int completedCount,
                                  int pendingCount)
        implements EmailSendable {
}
