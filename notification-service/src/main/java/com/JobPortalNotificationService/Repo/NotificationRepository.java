package com.JobPortalNotificationService.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JobPortalNotificationService.Entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

}
