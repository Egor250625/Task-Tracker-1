package com.tasktracker.emailservice.handler;


import com.tasktracker.emailservice.dto.request.EmailSendable;

public interface EmailHandler {
    void handle(EmailSendable dto);
}
