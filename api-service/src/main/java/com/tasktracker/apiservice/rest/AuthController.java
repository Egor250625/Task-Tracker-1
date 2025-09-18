package com.tasktracker.apiservice.rest;

import com.tasktracker.apiservice.dto.response.LoginResponse;
import com.tasktracker.apiservice.dto.request.UserCreateEditDto;
import com.tasktracker.apiservice.dto.request.UserLoginDto;
import com.tasktracker.apiservice.dto.response.UserReadDto;
import com.tasktracker.apiservice.security.CustomUserDetails;
import com.tasktracker.apiservice.service.UserService;
import com.tasktracker.apiservice.utils.JwtTokenUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(userDetails.getUsername(), token));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> registration(@Valid @RequestBody UserCreateEditDto dto) {
        UserReadDto createdUser = userService.create(dto);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(userDetails.getUsername(), token));
    }

}
