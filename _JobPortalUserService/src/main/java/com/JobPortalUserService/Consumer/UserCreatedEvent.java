package com.JobPortalUserService.Consumer;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
	

    // 🔑 REQUIRED for idempotency
    private String eventId;

    // 🔑 Helpful for routing / debugging
    private String eventType;

    private Instant createdAt;
	
    private Long authId;
    private String email;
    private String role;
}