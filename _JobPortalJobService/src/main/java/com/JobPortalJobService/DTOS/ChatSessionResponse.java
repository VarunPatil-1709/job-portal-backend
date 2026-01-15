package com.JobPortalJobService.DTOS;
import java.time.LocalDateTime;

public class ChatSessionResponse {

    private final String chatId;
    private final Long jobId;
    private final String jobTitle;
    private final LocalDateTime expiresAt;

    public ChatSessionResponse(
            String chatId,
            Long jobId,
            String jobTitle,
            LocalDateTime expiresAt
    ) {
        this.chatId = chatId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.expiresAt = expiresAt;
    }

    public String getChatId() {
        return chatId;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
