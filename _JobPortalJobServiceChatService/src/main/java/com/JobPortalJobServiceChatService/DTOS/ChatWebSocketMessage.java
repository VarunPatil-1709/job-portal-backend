package com.JobPortalJobServiceChatService.DTOS;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatWebSocketMessage {

    private String chatRoomId;
    private String senderName;
    private String content;
    private LocalDateTime timestamp;
}
