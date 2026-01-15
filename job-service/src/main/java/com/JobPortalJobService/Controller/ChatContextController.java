package com.JobPortalJobService.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.JobPortalJobService.DTOS.ChatParticipantsResponse;
import com.JobPortalJobService.Service.ChatContextService;

@RestController
@RequestMapping("/internal")
public class ChatContextController {

    private final ChatContextService chatContextService;

    public ChatContextController(ChatContextService chatContextService) {
        this.chatContextService = chatContextService;
    }

    /**
     * INTERNAL API
     * Used by Chat Service to resolve recruiter & candidate for a job chat
     */
    @GetMapping("/chat-participants")
    public ChatParticipantsResponse getChatParticipants(
            @RequestParam Long jobId,
            @RequestParam(required = false) Long candidateId,
            Authentication authentication
    ) {

        // 🔐 Extract userId from JWT (already validated by Spring Security)
        Long requesterId = extractUserId(authentication);

        return chatContextService.resolveParticipants(
                jobId,
                requesterId,
                candidateId
        );
    }


    private Long extractUserId(Authentication authentication) {
        // Example: if userId is stored as principal name
        return Long.valueOf(authentication.getName());

        // OR if you use a custom UserDetails:
        // return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
    }
}