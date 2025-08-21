package com.tasktracker.apiservice.exception;

public class EmailNotFoundException extends RuntimeException {
  public EmailNotFoundException(String message, String email) {
    super(message);
  }
}
