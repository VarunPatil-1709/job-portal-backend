package com.JobPortalUserService.Consumer;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "processed_events")
public class ProcessedEvent {

    @Id
    private String eventId;

    private LocalDateTime processedAt = LocalDateTime.now();

    protected ProcessedEvent() {}

    public ProcessedEvent(String eventId) {
        this.eventId = eventId;
    }
}