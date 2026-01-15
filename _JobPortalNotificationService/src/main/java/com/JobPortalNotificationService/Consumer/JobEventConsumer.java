package com.JobPortalNotificationService.Consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.JobPortalNotificationService.DTOS.JobEvent;
import com.JobPortalNotificationService.Entity.Notification;
import com.JobPortalNotificationService.Service.NotificationService;
import com.JobPortalNotificationService.Template.NotificationTemplate;

@Component
public class JobEventConsumer {

    private final NotificationService notificationService;

    public JobEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
        topics = { "job-notification", "chat-schedule" },
        groupId = "notification-service"
    )
    public void consume(JobEvent event) {

        if (event == null || event.getUserId() == null) {
            return;
        }

        Notification notification = new Notification();
        notification.setUserId(event.getUserId());
        notification.setType(event.getEventType());

        // ✅ Title is simple and event-based
        notification.setTitle(
                NotificationTemplate.title(event.getEventType())
        );

        // ✅ Message is STRUCTURED, not string-based
        notification.setMessage(
                NotificationTemplate.message(
                        event.getEventType(),
                        event.getJobTitle(),
                        event.getChatId(),
                        event.getStartAt(),
                        event.getEndAt()
                )
        );

        notificationService.save(notification);
    }
}
