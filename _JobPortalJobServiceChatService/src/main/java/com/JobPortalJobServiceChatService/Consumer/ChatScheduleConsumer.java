package com.JobPortalJobServiceChatService.Consumer;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.JobPortalJobServiceChatService.DTOS.JobEvent;
import com.JobPortalJobServiceChatService.Entity.ChatSession;
import com.JobPortalJobServiceChatService.Entity.ChatStatus;
import com.JobPortalJobServiceChatService.Repo.ChatSessionRepository;

@Component
public class ChatScheduleConsumer {

    private final ChatSessionRepository repository;

    public ChatScheduleConsumer(ChatSessionRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(
        topics = "chat-schedule",
        groupId = "chat-service"
    )
    public void consume(JobEvent event) {

        // ✅ Correct event type
        if (!"CHAT_SESSION_SCHEDULED".equals(event.getEventType())) {
            return;
        }

        // ✅ Idempotency (VERY IMPORTANT)
        if (repository.existsById(event.getChatId())) {
            return;
        }

        ChatSession session = new ChatSession();
        session.setChatId(event.getChatId());
        session.setJobId(event.getJobId());
        session.setCandidateId(event.getUserId());
        session.setRecruiterId(event.getRecruiterId());

        // ⏱️ authoritative window
        session.setStartAt(event.getStartAt());
        session.setEndAt(event.getEndAt());

        // 🕒 scheduled, not active yet
        session.setStatus(ChatStatus.CREATED);

        repository.save(session);

        System.out.println(
            "✅ Chat session scheduled: " + event.getChatId()
            + " | starts at: " + event.getStartAt()
        );
    }
}
