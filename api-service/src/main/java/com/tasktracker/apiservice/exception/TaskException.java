package com.tasktracker.apiservice.exception;

public class TaskException extends RuntimeException {
    public TaskException(String message) {
        super(message);
    }
}
