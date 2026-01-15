package com.JobPortalJobService.Entity;

public enum ChatSessionStatus {
    CREATED,   // recruiter initiated
    ACTIVE,    // both users joined
    CLOSED     // expired or disconnected
}
