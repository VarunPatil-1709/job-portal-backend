package com.JobPortalJobService.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.JobPortalJobService.Client.RecruiterClient;
import com.JobPortalJobService.DTOS.CompanySnapshot;
import com.JobPortalJobService.DTOS.CreateJobRequest;
import com.JobPortalJobService.Entity.JobEntity;
import com.JobPortalJobService.Repo.JobPostingRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobPostingRepo jobRepository;
    private final RecruiterClient recruiterClient;

    // ===============================
    // CREATE JOB (SNAPSHOT PATTERN)
    // ===============================
    @Transactional
    public void createJob(CreateJobRequest request) {

        // 1️⃣ Business validation
        if (request.getMinSalary() > request.getMaxSalary()) {
            throw new IllegalArgumentException(
                    "minSalary cannot be greater than maxSalary"
            );
        }

        // 2️⃣ Auth identity (already verified by JwtAuthenticationFilter)
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Unauthenticated request");
        }

        Long recruiterAuthId =
                Long.parseLong(authentication.getName());

        // 3️⃣ Fetch recruiter snapshot from User Service
        CompanySnapshot snapshot =
                recruiterClient.getCompanySnapshot();

        if (snapshot == null || snapshot.getCompanyName() == null) {
            throw new IllegalStateException(
                    "Recruiter profile incomplete or not found"
            );
        }

        // 4️⃣ Create Job entity
        JobEntity job = new JobEntity();
        job.setRecruiterAuthId(recruiterAuthId);

        // 📸 Snapshot copy (denormalized data)
        job.setCompanyName(snapshot.getCompanyName());
        job.setCompanySize(snapshot.getCompanySize());
        job.setCompanyWebsite(snapshot.getCompanyWebsite());

        // 📄 Job details
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setEmploymentType(request.getEmploymentType());
        job.setExperienceRequired(request.getExperienceRequired());
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());

        job.setPostedAt(LocalDateTime.now());
        job.setActive(true);

        // 5️⃣ Persist
        jobRepository.save(job);
    }

    // ===============================
    // READ APIs
    // ===============================

    public List<JobEntity> getAllActiveJobs() {
        return jobRepository.findByActiveTrue();
    }

    public JobEntity getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Job not found")
                );
    }

    public List<JobEntity> getJobsByRecruiter() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Unauthenticated request");
        }

        Long recruiterAuthId =
                Long.parseLong(authentication.getName());

        return jobRepository.findByRecruiterAuthId(recruiterAuthId);
    }
}