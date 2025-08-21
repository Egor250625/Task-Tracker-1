package com.tasktracker.emailservice.dto.request;

public interface EmailSendable {
    String to();
    String type();
}
