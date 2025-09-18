package com.tasktracker.apiservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserCreateEditDto(@Email
                                String email,
                                @Size(min = 5, max = 20, message = "Password must be between 5 and 20 characters long.")
                                String password) {
}