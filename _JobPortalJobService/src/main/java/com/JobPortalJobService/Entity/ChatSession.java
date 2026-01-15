package com.JobPortalJobService.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_sessions")
public class ChatSession {

    @Id
    @Column(name = "chat_id", nullable = false, updatable = false)
    private String chatId;

    @Column(nullable = false)
    private Long jobId;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private Long recruiterId;

    @Column(nullable = false)
    private Long candidateId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatSessionStatus status;

    protected ChatSession() {} // JPA

    private ChatSession(
            String chatId,
            Long jobId,
            String jobTitle,
            Long recruiterId,
            Long candidateId,
            LocalDateTime createdAt,
            LocalDateTime startAt,
            LocalDateTime endAt,
            ChatSessionStatus status
    ) {
        this.chatId = chatId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.recruiterId = recruiterId;
        this.candidateId = candidateId;
        this.createdAt = createdAt;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
    }

    /* ================= FACTORY ================= */

    public static ChatSession create(
            Long jobId,
            String jobTitle,
            Long recruiterId,
            Long candidateId,
            int startDelayMinutes
    ) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startAt = now.plusMinutes(startDelayMinutes);
        LocalDateTime endAt = startAt.plusMinutes(30);

        return new ChatSession(
                generateChatId(),
                jobId,
                jobTitle,
                recruiterId,
                candidateId,
                now,
                startAt,
                endAt,
                ChatSessionStatus.CREATED
        );
    }

    private static String generateChatId() {
        return "CHAT-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    /* ================= DOMAIN RULES ================= */

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startAt) && now.isBefore(endAt);
    }

    public void markActive() {
        if (!isActive()) {
            throw new IllegalStateException("Chat is not in active window");
        }
        this.status = ChatSessionStatus.ACTIVE;
    }

    public void autoCloseIfExpired() {
        if (LocalDateTime.now().isAfter(endAt)) {
            this.status = ChatSessionStatus.CLOSED;
        }
    }

    /* ================= GETTERS ================= */

    public String getChatId() { return chatId; }
    public Long getJobId() { return jobId; }
    public String getJobTitle() { return jobTitle; }
    public Long getRecruiterId() { return recruiterId; }
    public Long getCandidateId() { return candidateId; }
    public LocalDateTime getStartAt() { return startAt; }
    public LocalDateTime getEndAt() { return endAt; }
    public ChatSessionStatus getStatus() { return status; }
}
