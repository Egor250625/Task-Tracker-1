package com.tasktracker.apiservice.exception;

import jakarta.validation.constraints.Email;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String message, @Email String email) {
        super(message);
    }
}
