package com.tasktracker.apiservice.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.tasktracker.apiservice.converter.JsonNodeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_events")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "pull_count")
    private int pullCount;

    @Column(name = "enqueued_at")
    private LocalDateTime enqueuedAt;

    @Column(name = "reserved_to")
    private LocalDateTime reservedTo;

    @Column(name = "message", columnDefinition = "jsonb", nullable = false)
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode message;

}
