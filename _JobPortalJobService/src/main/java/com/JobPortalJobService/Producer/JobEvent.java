package com.JobPortalJobService.Producer;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobEvent implements Serializable {

    // 🔹 Existing (do not break consumers)
    private String eventType;
    private Long userId;        // candidateAuthId
    private Long recruiterId;
    private String jobTitle;
    private LocalDateTime eventTime;

    // 🔹 NEW (chat-specific, optional for other events)
    private String chatId;
    private Long jobId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public JobEvent() {
        this.eventTime = LocalDateTime.now();
    }

    // ✅ Old constructor (keep)
    public JobEvent(String eventType, Long userId, Long recruiterId, String jobTitle) {
        this.eventType = eventType;
        this.userId = userId;
        this.recruiterId = recruiterId;
        this.jobTitle = jobTitle;
        this.eventTime = LocalDateTime.now();
    }

    // ✅ Old constructor (keep)
    public JobEvent(String eventType, Long userId, String jobTitle) {
        this.eventType = eventType;
        this.userId = userId;
        this.jobTitle = jobTitle;
        this.eventTime = LocalDateTime.now();
    }

    // ✅ NEW constructor for CHAT EVENTS
    public JobEvent(
            String eventType,
            Long userId,
            Long recruiterId,
            Long jobId,
            String chatId,
            String jobTitle,
            LocalDateTime startAt,
            LocalDateTime endAt
    ) {
        this.eventType = eventType;
        this.userId = userId;
        this.recruiterId = recruiterId;
        this.jobId = jobId;
        this.chatId = chatId;
        this.jobTitle = jobTitle;   // ✅ now set
        this.startAt = startAt;
        this.endAt = endAt;
        this.eventTime = LocalDateTime.now();
    }

}
