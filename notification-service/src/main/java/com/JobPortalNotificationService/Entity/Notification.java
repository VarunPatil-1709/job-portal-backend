package com.JobPortalNotificationService.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	private String title;
	
    @Column(columnDefinition = "TEXT")
    private String message;

    private String type;

    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

}
