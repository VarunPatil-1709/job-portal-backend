package com.JobPortalJobService.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "job_applications",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"job_id", "user_auth_id"})
    }
)
@Getter
@Setter
public class JobApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "user_auth_id", nullable = false)
    private Long userAuthId;


    @Column(name = "resume_ref")
    private String resumeRef; 

    @Column(name = "resume_source", nullable = false)
    private String resumeSource;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(nullable = false)
    private LocalDateTime appliedAt;
}