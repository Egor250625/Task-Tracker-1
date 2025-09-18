package com.tasktracker.apiservice.utils;

import com.tasktracker.apiservice.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    public String generateToken(CustomUserDetails customUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());
        String token = Jwts.builder()
                .setSubject(customUserDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        log.info("Generated JWT token for user '{}', expires at {}", customUserDetails.getUsername(), expiredDate);
        return token;
    }

    public String getUsername(String token) {
        log.info("Extracting username from token: {}", token);
        try {
            String username = getAllClaimsFromToken(token).getSubject();
            log.info("Username extracted from token: {}", username);
            return username;
        } catch (Exception e) {
            log.error("Failed to extract username from token", e);
            throw new JwtException("Failed to extract username from token");
        }
    }


    private Claims getAllClaimsFromToken(String token) {
        log.debug("Parsing claims from token: {}", token);
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
