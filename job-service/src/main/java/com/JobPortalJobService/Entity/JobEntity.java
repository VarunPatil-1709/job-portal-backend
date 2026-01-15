package com.JobPortalJobService.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Recruiter Identity =====
    @Column(nullable = false)
    private Long recruiterAuthId;

    // ===== Company Snapshot =====
    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String companySize;

    @Column(nullable = false)
    private String companyWebsite;

    // ===== Job Details =====
    @Column(nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description; // supports 500+ words

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String employmentType; 
    // FULL_TIME, PART_TIME, INTERN, CONTRACT

    @Column(nullable = false)
    private Integer experienceRequired; // in years

    // ===== Salary (CORRECT WAY) =====
    @Column(nullable = false)
    private Integer minSalary;

    @Column(nullable = false)
    private Integer maxSalary;


    // ===== Meta =====
    @Column(nullable = false)
    private LocalDateTime postedAt;

    @Column(nullable = false)
    private Boolean active = true;
}