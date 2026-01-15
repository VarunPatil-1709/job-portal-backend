package com.JobPortalNotificationService.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.JobPortalNotificationService.Entity.Notification;
import com.JobPortalNotificationService.Repo.NotificationRepository;

import jakarta.transaction.Transactional;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void save(Notification notification) {
        repository.save(notification);
    }

    @Transactional
    public List<Notification> getUserNotifications(Long authId) {

        return repository
                .findByUserIdOrderByCreatedAtDesc(authId);
    }

    public void markAsRead(Long id) {
        Notification n = repository.findById(id).orElseThrow();
        n.setRead(true);
        repository.save(n);
    }
}