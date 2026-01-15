package com.JobPortalJobServiceChatService.DTOS;

import lombok.Getter;

import lombok.Setter;
@Getter
@Setter
public class ChatMessageDTO {
    private String chatRoomId;
    private Long receiverId;
    private String content;
}
