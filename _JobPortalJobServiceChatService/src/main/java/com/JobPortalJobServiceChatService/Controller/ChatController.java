package com.JobPortalJobServiceChatService.Controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.JobPortalJobServiceChatService.DTOS.ChatSendRequest;
import com.JobPortalJobServiceChatService.DTOS.ChatWebSocketMessage;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void send(ChatSendRequest dto) {

        if (dto.getSenderName() == null || dto.getSenderName().isBlank()) {
            return;
        }

        ChatWebSocketMessage payload =
                new ChatWebSocketMessage(
                        dto.getChatRoomId(),
                        dto.getSenderName(),
                        dto.getContent(), null
                );

        messagingTemplate.convertAndSend(
                "/topic/chat." + dto.getChatRoomId(),
                payload
        );
    }
}
