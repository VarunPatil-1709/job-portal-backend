package com.JobPortalAuthService.Producer;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "outbox_event")
@Getter
@Setter
public class OutboxEvent {

    @Id
    private String id;        // same as eventId

    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String status;    // NEW, SENT, FAILED

    private Instant createdAt;
}
