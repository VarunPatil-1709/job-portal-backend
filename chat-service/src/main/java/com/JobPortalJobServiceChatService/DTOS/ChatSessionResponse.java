package com.JobPortalJobServiceChatService.DTOS;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatSessionResponse {

    private String chatId;
    private Long jobId;
    private String status;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Long otherUserId;

    // getters & setters
}