package com.JobPortalJobServiceChatService.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.JobPortalJobServiceChatService.DTOS.ChatSessionResponse;
import com.JobPortalJobServiceChatService.Entity.ChatSession;
import com.JobPortalJobServiceChatService.Repo.ChatSessionRepository;

@Service
public class ChatSessionService {

    private final ChatSessionRepository repo;

    public ChatSessionService(ChatSessionRepository repo) {
        this.repo = repo;
    }

    public List<ChatSessionResponse> getUserChats(
            Long userId,
            String type,
            Long jobId
    ) {

        if (type == null) type = "ALL";

        return repo.findUserChats(userId, type, jobId)
                .stream()
                .map(c -> map(c, userId))
                .collect(Collectors.toList());
    }

    private ChatSessionResponse map(ChatSession c, Long userId) {
        ChatSessionResponse dto = new ChatSessionResponse();
        dto.setChatId(c.getChatId());
        dto.setJobId(c.getJobId());
        dto.setStatus(c.getStatus().name());
        dto.setStartAt(c.getStartAt());
        dto.setEndAt(c.getEndAt());

        // 👇 figure out the other user
        dto.setOtherUserId(
                c.getCandidateId().equals(userId)
                        ? c.getRecruiterId()
                        : c.getCandidateId()
        );

        return dto;
    }
}