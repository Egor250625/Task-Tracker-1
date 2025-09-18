package com.tasktracker.apiservice.rest;

import com.tasktracker.apiservice.dto.response.UserReadDto;
import com.tasktracker.apiservice.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @GetMapping("/user")
    public UserReadDto getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new UserReadDto(userDetails.getUsername());
    }
}
