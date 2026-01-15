package com.JobPortalNotificationService.Controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.JobPortalNotificationService.Entity.Notification;
import com.JobPortalNotificationService.Service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    /**
     * ===============================
     * GET MY NOTIFICATIONS
     * ===============================
     * authId is extracted from JWT by SecurityFilter
     */
    @GetMapping("/me")
    public List<Notification> getMyNotifications(Authentication authentication) {

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Unauthenticated request");
        }

        Long authId = Long.parseLong(authentication.getName());
        return service.getUserNotifications(authId);
    }

    /**
     * ===============================
     * MARK NOTIFICATION AS READ
     * ===============================
     */
    @PostMapping("/{id}/read")
    public void markRead(
            @PathVariable Long id,
            Authentication authentication
    ) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Unauthenticated request");
        }

        service.markAsRead(id);
    }
}
