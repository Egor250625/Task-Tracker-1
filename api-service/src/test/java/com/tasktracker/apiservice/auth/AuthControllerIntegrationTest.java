package com.tasktracker.apiservice.auth;

import com.tasktracker.apiservice.kafka.producer.EmailKafkaProducer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AuthControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Mock
    private EmailKafkaProducer emailKafkaProducer;

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }


    Map
            <java.lang.String,
                    java.lang.String> user = Map.of(
            "email", "newuser@example.com",
            "password", "password123"
    );

    @Test
    void register_Success() {
        ResponseEntity<Map> response = restTemplate.postForEntity(
                getUrl("/api/auth/register"), user, Map.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().get("token"));
    }

    @Test
    void login_Success() {
        var registerReq = user;
        restTemplate.postForEntity(getUrl("/api/auth/register"), registerReq, Map.class);
        var loginReq = user;
        ResponseEntity<Map> response = restTemplate.postForEntity(getUrl("/api/auth/register"), loginReq, Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().get("token"));
    }

}
