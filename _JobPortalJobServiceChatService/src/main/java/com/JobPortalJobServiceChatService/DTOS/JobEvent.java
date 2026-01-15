package com.JobPortalJobServiceChatService.DTOS;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobEvent {

    private String eventType;

    // candidate
    private Long userId;

    // recruiter
    private Long recruiterId;

    // job reference
    private Long jobId;

    // chat reference
    private String chatId;

    // optional (notification / UI)
    private String jobTitle;

    // scheduling window
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private LocalDateTime eventTime;
}
