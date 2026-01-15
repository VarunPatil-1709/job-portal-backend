package com.JobPortalJobServiceChatService.Config;

import java.time.LocalDateTime;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.JobPortalJobServiceChatService.Entity.ChatSession;
import com.JobPortalJobServiceChatService.Entity.ChatStatus;
import com.JobPortalJobServiceChatService.Repo.ChatSessionRepository;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final ChatSessionRepository chatRepo;

    public WebSocketAuthInterceptor(JwtUtil jwtUtil,
                                    ChatSessionRepository chatRepo) {
        this.jwtUtil = jwtUtil;
        this.chatRepo = chatRepo;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {


        return message;
    }


    /* ================= COOKIE HELPER ================= */

    private String extractJwtFromCookies(HttpServletRequest request) {
        if (request == null || request.getCookies() == null) return null;

        for (Cookie c : request.getCookies()) {
            if ("jwt".equals(c.getName())) { // ⚠️ ensure cookie name
                return c.getValue();
            }
        }
        return null;
    }
    
}
