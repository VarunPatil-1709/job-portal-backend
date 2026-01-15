package com.JobPortalAuthService.Producer;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEvent {
	
    // 🔑 REQUIRED for idempotency
    private String eventId;

    // 🔑 Helps routing & debugging
    private String eventType;

    // 🔑 Traceability
    private Instant createdAt;
	
    private Long authId;
    private String email;
    private String role;
}