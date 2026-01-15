package com.JobPortalNotificationService.DTOS;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobEvent implements Serializable {

    private String eventType;
    private Long userId;
    private Long recruiterId;

    private Long jobId;
    private String chatId;
    private String jobTitle;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private LocalDateTime eventTime;
}
