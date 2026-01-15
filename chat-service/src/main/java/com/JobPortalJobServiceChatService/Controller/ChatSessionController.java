package com.JobPortalJobServiceChatService.Controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.JobPortalJobServiceChatService.Config.JwtUtil;
import com.JobPortalJobServiceChatService.DTOS.ChatSessionResponse;
import com.JobPortalJobServiceChatService.Service.ChatSessionService;


@RestController
@CrossOrigin
public class ChatSessionController {

    private final ChatSessionService service;
    private final JwtUtil jwtUtil;

    public ChatSessionController(ChatSessionService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/chat/sessions")
    public List<ChatSessionResponse> getChats(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long jobId,
            HttpServletRequest request
    ) {
        // ✅ Extract JWT from cookie
        String token = jwtUtil.extractTokenFromRequest(request);

        // ✅ Extract userId from token
        Long userId = jwtUtil.extractUserId(token);

        return service.getUserChats(userId, type, jobId);
    }
}
