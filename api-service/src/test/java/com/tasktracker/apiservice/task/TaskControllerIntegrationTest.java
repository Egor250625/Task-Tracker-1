package com.tasktracker.apiservice.task;

import com.tasktracker.apiservice.dto.request.TaskCreateRequestDto;
import com.tasktracker.apiservice.dto.request.TaskDeleteRequestDto;
import com.tasktracker.apiservice.dto.request.TaskUpdateRequestDto;
import com.tasktracker.apiservice.dto.response.TaskResponseDto;
import com.tasktracker.apiservice.model.Status;
import com.tasktracker.apiservice.model.entity.Task;
import com.tasktracker.apiservice.model.entity.User;
import com.tasktracker.apiservice.repository.TaskRepository;
import com.tasktracker.apiservice.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }

    private int testUserId;

    @BeforeEach
    void setUp() {
        // Создаём тестового пользователя
        var user = new User();
        user.setEmail("taskuser@example.com");
        user.setPassword("password");
        userRepository.save(user);
        testUserId = user.getId();
    }

    @AfterEach
    void tearDown() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    void createTask_Success() {
        var request = new TaskCreateRequestDto("Test Task", "Description", Status.IN_PROGRESS);
        ResponseEntity<TaskResponseDto> response = restTemplate.postForEntity(
                getUrl("/api/task"),
                request,
                TaskResponseDto.class
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Task", response.getBody().title());
    }

    @Test
    @Order(2)
    void getTasks_Success() {
        // Создаём таску для проверки
        Task task = new Task();
        task.setTitle("Sample Task");
        task.setStatus(Status.IN_PROGRESS.name());
        task.setOwner(userRepository.findById(testUserId).get());
        taskRepository.save(task);

        ResponseEntity<TaskResponseDto[]> response = restTemplate.getForEntity(
                getUrl("/api/tasks"),
                TaskResponseDto[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    @Order(3)
    void updateTask_Success() {
        Task task = new Task();
        task.setTitle("Old Task");
        task.setStatus(Status.IN_PROGRESS.name());
        task.setOwner(userRepository.findById(testUserId).get());
        Task saved = taskRepository.save(task);

        var updateRequest = new TaskUpdateRequestDto(saved.getId(), "Updated Task", "Updated Desc", Status.COMPLETED);
        restTemplate.patchForObject(getUrl("/api/task"), updateRequest, TaskResponseDto.class);

        Task updated = taskRepository.findById(saved.getId()).get();
        assertEquals("Updated Task", updated.getTitle());
        assertEquals(Status.COMPLETED.name(), updated.getStatus());
    }

    @Test
    @Order(4)
    void deleteTask_Success() {
        Task task = new Task();
        task.setTitle("ToDelete");
        task.setStatus(Status.IN_PROGRESS.name());
        task.setOwner(userRepository.findById(testUserId).get());
        Task saved = taskRepository.save(task);

        HttpEntity<TaskDeleteRequestDto> request = new HttpEntity<>(new TaskDeleteRequestDto(saved.getId()));
        restTemplate.exchange(getUrl("/api/task"), HttpMethod.DELETE, request, Void.class);

        assertFalse(taskRepository.findById(saved.getId()).isPresent());
    }
}
