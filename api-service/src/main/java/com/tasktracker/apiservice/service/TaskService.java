package com.tasktracker.apiservice.service;

import com.tasktracker.apiservice.dto.request.TaskCreateRequestDto;
import com.tasktracker.apiservice.dto.request.TaskDeleteRequestDto;
import com.tasktracker.apiservice.dto.request.TaskUpdateRequestDto;
import com.tasktracker.apiservice.dto.response.TaskResponseDto;
import com.tasktracker.apiservice.exception.TaskException;
import com.tasktracker.apiservice.mapper.TaskMapper;
import com.tasktracker.apiservice.model.Status;
import com.tasktracker.apiservice.model.entity.Task;
import com.tasktracker.apiservice.model.entity.User;
import com.tasktracker.apiservice.repository.TaskRepository;
import com.tasktracker.apiservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public TaskResponseDto createTask(TaskCreateRequestDto taskDto, int userId) {
        return Optional.of(taskDto).map(dto -> {
                    Task task = taskMapper.toEntity(taskDto);
                    task.setOwner(userRepository.findUserById(userId));
                    task.setStatus(Status.IN_PROGRESS.name());
                    log.info("Task mapped successfully with title = {}", task.getTitle());
                    return taskRepository.save(task);
                })
                .map(taskMapper::toDto)
                .orElseThrow(() -> {
                    log.error("Exception during creating a task with title  = {}", taskDto.title());
                    return new TaskException("Exception during creating a task");
                });
    }

    public List<TaskResponseDto> getAllTasks(int userId) {
        User owner = userRepository.findUserById(userId);
        Optional.of(owner).orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        List<Task> tasks = taskRepository.findAllByOwner(owner);
        log.info("Total tasks for owner {},found = {}", owner.getEmail(), tasks.size());
        return tasks.stream().map(taskMapper::toDto).toList();
    }

    @Transactional
    public TaskResponseDto updateTask(TaskUpdateRequestDto dto, int userId) {
        Task task = authenticateUserAndTask(dto.id(), userId);

        if (dto.status() != null && !EnumSet.of(Status.IN_PROGRESS, Status.COMPLETED).contains(dto.status())) {
            throw new IllegalArgumentException("Недопустимый статус задачи");
        }

        if (dto.title() != null) {
            task.setTitle(dto.title());
            task.setDescription(dto.description());
            task.setStatus(dto.status().name());
        }

        if (task.getStatus().equals(Status.COMPLETED.name())) {
            task.setDoneAt(LocalDateTime.now());
        }
        taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Transactional
    public void deleteTask(TaskDeleteRequestDto dto, int userId) {
        Task task = authenticateUserAndTask(dto.id(), userId);
        taskRepository.delete(task);
        log.info("Task deleted successfully with id = {}", dto.id());
    }


    private Task authenticateUserAndTask(int taskId, int userId) {
        return taskRepository.findById(taskId)
                .filter(t -> t.getOwner().getId() == userId)
                .orElseThrow(() -> new TaskException("Task was not found or access denied."));
    }
}
