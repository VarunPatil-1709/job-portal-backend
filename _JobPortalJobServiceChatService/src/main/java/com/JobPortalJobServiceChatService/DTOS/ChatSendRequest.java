package com.JobPortalJobServiceChatService.DTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor

public class ChatSendRequest {
    private String chatRoomId;
    private String senderName;
    private String content;
    
	public ChatSendRequest(String chatRoomId, String senderName, String content) {
		super();
		this.chatRoomId = chatRoomId;
		this.senderName = senderName;
		this.content = content;
	}
    
    
    
}
