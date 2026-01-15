package com.JobPortalJobService.DTOS;

import java.time.LocalDateTime;

public class ChatSessionRequest {

    private Long candidateAuthId;

    /**
     * Requested chat start time (optional).
     * Server will validate and adjust if needed.
     */
    private LocalDateTime startAt;

    public Long getCandidateAuthId() {
        return candidateAuthId;
    }

    public void setCandidateAuthId(Long candidateAuthId) {
        this.candidateAuthId = candidateAuthId;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }
}
