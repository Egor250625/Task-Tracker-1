package com.tasktracker.apiservice.security;

import com.tasktracker.apiservice.exception.EmailNotFoundException;
import com.tasktracker.apiservice.model.entity.User;
import com.tasktracker.apiservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Custom user detail process started...");
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EmailNotFoundException("email not found with parameters = {}", email)
        );
        return new CustomUserDetails(user);
    }
}
