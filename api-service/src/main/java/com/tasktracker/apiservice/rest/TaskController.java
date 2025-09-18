package com.tasktracker.apiservice.rest;

import com.tasktracker.apiservice.dto.request.TaskCreateRequestDto;
import com.tasktracker.apiservice.dto.request.TaskDeleteRequestDto;
import com.tasktracker.apiservice.dto.request.TaskUpdateRequestDto;
import com.tasktracker.apiservice.dto.request.UserInformationDto;
import com.tasktracker.apiservice.dto.response.TaskResponseDto;
import com.tasktracker.apiservice.kafka.producer.TaskNotificationKafkaProducer;
import com.tasktracker.apiservice.security.CustomUserDetails;
import com.tasktracker.apiservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskNotificationKafkaProducer taskNotificationKafkaProducer;

    @PostMapping("/task")
    public ResponseEntity<TaskResponseDto> addTask(@RequestBody TaskCreateRequestDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(dto, userDetails.getId()));
    }

    @GetMapping("/tasks")
    public ResponseEntity<Page<TaskResponseDto>> getTasks(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(taskService.getAllTasks(userDetails.getId(),page,size));
    }

    @PatchMapping("/task")
    public ResponseEntity<TaskResponseDto> updateTask(@RequestBody TaskUpdateRequestDto dto,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(taskService.updateTask(dto, userDetails.getId()));
    }

    @DeleteMapping("/task")
    public void deleteTask(@RequestBody TaskDeleteRequestDto dto,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        taskService.deleteTask(dto, userDetails.getId());
    }

    @PostMapping("/task/info")
    public ResponseEntity<CustomUserDetails> getAllTasksInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        var dto = new UserInformationDto(userDetails.getId());
        taskNotificationKafkaProducer.send(dto);
        return ResponseEntity.ok(userDetails);
    }
}
