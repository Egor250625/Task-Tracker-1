package com.tasktracker.apiservice.advice;

import com.tasktracker.apiservice.dto.response.ApiErrorResponse;
import com.tasktracker.apiservice.exception.EmailAlreadyExistException;
import com.tasktracker.apiservice.exception.EmailNotFoundException;
import com.tasktracker.apiservice.exception.TaskException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExist(EmailAlreadyExistException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Account with this email already exists"));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmailNotFound(EmailNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Email not found"));
    }

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<Map<String, String>> handleTaskException(TaskException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(String message, HttpStatus status, String path) {
        ApiErrorResponse response = new ApiErrorResponse(message, status.value(), path);
        return ResponseEntity.status(status).body(response);
    }
}
