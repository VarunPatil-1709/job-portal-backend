package com.JobPortalAuthService.Services;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.JobPortalAuthService.Producer.OutboxEvent;
import com.JobPortalAuthService.Repo.OutboxRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    /**
     * Save event to outbox table.
     * This method MUST be called inside a @Transactional business method.
     */
    public void saveEvent(String eventId, String eventType, String payload) {

        OutboxEvent event = new OutboxEvent();
        event.setId(eventId);              // same as eventId
        event.setEventType(eventType);     // USER_CREATED
        event.setPayload(payload);         // JSON string
        event.setStatus("NEW");            // NEW | SENT | FAILED
        event.setCreatedAt(Instant.now());

        outboxRepository.save(event);
    }
}