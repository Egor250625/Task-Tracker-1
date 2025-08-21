package com.tasktracker.schedulerservice.service;

import com.tasktracker.schedulerservice.dto.request.TaskNotificationDto;
import com.tasktracker.schedulerservice.entity.Task;
import com.tasktracker.schedulerservice.entity.User;
import com.tasktracker.schedulerservice.kafka.TaskNotificationProducer;
import com.tasktracker.schedulerservice.repository.TaskRepository;
import com.tasktracker.schedulerservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final LocalDateTime since = LocalDateTime.now().minusDays(1);
    private final TaskNotificationProducer taskNotificationProducer;


    public void sendUserTasksSummary(int id) {
        User user = Optional.ofNullable(userRepository.getUserById(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        log.info("User get successfully from DB with email {}", user.getEmail());

        var dto = createTaskNotification(user);

        taskNotificationProducer.send(dto);
    }

    @Scheduled(cron = "0 0 22 * * *", zone = "Europe/Moscow")
    public void sendDailyTasksSummaryToAllUsers() {
        userRepository.findAll().stream()
                .map(User::getId)
                .forEach(this::sendUserTasksSummary);
    }


    private TaskNotificationDto createTaskNotification(User user) {
        List<String> completedTasksForDay = taskRepository.findCompletedSince(user, since)
                .stream()
                .map(Task::getTitle)
                .limit(5)
                .toList();
        List<String> inProgressTasks = taskRepository.findInProgress(user)
                .stream()
                .map(Task::getTitle)
                .limit(5)
                .toList();

        if (completedTasksForDay.isEmpty() && inProgressTasks.isEmpty()) {
            log.info("No tasks to send for user {}", user.getEmail());
            throw new RuntimeException();
        }

        Map<String, List<String>> tasksMap = new HashMap<>();
        tasksMap.put("COMPLETED", completedTasksForDay);
        tasksMap.put("IN_PROGRESS", inProgressTasks);

        return new TaskNotificationDto(
                user.getEmail(),
                "TASK_REPORT",
                tasksMap,
                completedTasksForDay.size(),
                inProgressTasks.size()
        );
    }

}
