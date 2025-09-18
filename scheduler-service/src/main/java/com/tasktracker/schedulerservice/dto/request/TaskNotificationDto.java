package com.tasktracker.schedulerservice.dto.request;

import java.util.List;
import java.util.Map;

public record TaskNotificationDto(String to,
                                  String type,
                                  Map<String, List<String>> tasks,
                                  int completedCount,
                                  int pendingCount) {
}
