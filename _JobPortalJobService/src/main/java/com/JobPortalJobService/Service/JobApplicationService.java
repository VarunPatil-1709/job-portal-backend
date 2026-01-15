package com.JobPortalJobService.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.JobPortalJobService.DTOS.ChatSessionResponse;
import com.JobPortalJobService.DTOS.UserApplicationStats;
import com.JobPortalJobService.Entity.ApplicationStatus;
import com.JobPortalJobService.Entity.JobApplicationEntity;
import com.JobPortalJobService.Entity.JobEntity;
import com.JobPortalJobService.Producer.JobEvent;
import com.JobPortalJobService.Producer.JobEventProducer;
import com.JobPortalJobService.Repo.JobApplicationRepo;
import com.JobPortalJobService.Repo.JobPostingRepo;
import com.JobPortalJobService.S3Config.ResumeS3Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepo applicationRepo;
    private final JobPostingRepo jobRepo;

    private final JobEventProducer jobEventProducer;
    
    private final ResumeS3Service resumeS3Service;

    // ===============================
    // APPLY TO JOB (STUDENT)
    // ===============================
    @Transactional
    public void applyToJob(
            Long jobId,
            Long userAuthId,
            String resumeRef
    ) {

        // 1️⃣ Job must exist
        if (!jobRepo.existsById(jobId)) {
            throw new IllegalArgumentException("Job does not exist");
        }

        // 2️⃣ Prevent duplicate application
        if (applicationRepo.existsByJobIdAndUserAuthId(jobId, userAuthId)) {
            throw new IllegalStateException("Already applied to this job");
        }

        // 3️⃣ Resume is MANDATORY
        if (resumeRef == null || resumeRef.isBlank()) {
            throw new IllegalArgumentException("Resume is required");
        }

        // 4️⃣ 🔐 CRITICAL SECURITY CHECK
        // Ensure resume belongs to this user
        if (!resumeRef.startsWith("resumes/" + userAuthId + "/")) {
            throw new SecurityException("Invalid resume reference");
        }

        // 5️⃣ Create application
        JobApplicationEntity application = new JobApplicationEntity();
        application.setJobId(jobId);
        application.setUserAuthId(userAuthId);
        application.setResumeRef(resumeRef);
        application.setResumeSource("S3"); // ✅ correct source
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(LocalDateTime.now());

        applicationRepo.save(application);
    }


    // ===============================
    // STUDENT: MY APPLICATIONS
    // ===============================
    public List<JobApplicationEntity> getMyApplications(Long userAuthId) {
        return applicationRepo.findByUserAuthId(userAuthId);
    }

    // ===============================
    // RECRUITER: VIEW APPLICANTS
    // ===============================
    public List<JobApplicationEntity> getApplicantsForJob(Long jobId) {
        return applicationRepo.findByJobId(jobId);
    }
    /*
    @Transactional
    public void updateApplicationStatus(
    		Long userAuthId,
            Long jobId,
            ApplicationStatus newStatus) {
    	
    	JobApplicationEntity job = applicationRepo.findByJobIdAndUserAuthId(jobId, userAuthId)
    			.orElseThrow(() -> new RuntimeException("Application not found"));
    	
    	if(job.getStatus()==ApplicationStatus.REJECTED || job.getStatus()== ApplicationStatus.SELECTED) {
    		throw new IllegalStateException("Cannot change final status");
    	}
    	job.setStatus(newStatus);	
    }*/
    
    //Change is here
    @Transactional
    public void updateApplicationStatus(
            Long userAuthId,
            Long jobId,
            ApplicationStatus status
    ) {

        JobApplicationEntity application =
                applicationRepo.findByJobIdAndUserAuthId(jobId, userAuthId)
                        .orElseThrow(() -> new RuntimeException("Application not found"));

        if (application.getStatus() == ApplicationStatus.REJECTED ||
            application.getStatus() == ApplicationStatus.SHORTLISTED) {
            throw new IllegalStateException("Final state already reached");
        }

        application.setStatus(status);
        applicationRepo.save(application);

        // 🔔 PUBLISH EVENT (THIS WAS MISSING)
        if (status == ApplicationStatus.REJECTED ||
            status == ApplicationStatus.SHORTLISTED) {

            JobEntity job = jobRepo.findById(jobId)
                    .orElseThrow(() -> new RuntimeException("Job not found"));

            JobEvent event = new JobEvent(
                    "JOB_" + status.name(),
                    userAuthId,
                    job.getTitle()
            );

            jobEventProducer.publish(event);
        }
    }

//Till here
    
    
    public UserApplicationStats getStats(Long userAuthId) {
        return applicationRepo.getUserApplicationStats(userAuthId);
    }
    
    
    public List<JobApplicationEntity> myApplied(Long userAuthId) {
        return applicationRepo.findByUserAuthIdAndStatus(
                userAuthId,
                ApplicationStatus.APPLIED
        );
    }
    public List<JobApplicationEntity> myRejected(Long userAuthId) {
        return applicationRepo.findByUserAuthIdAndStatus(
                userAuthId,
                ApplicationStatus.REJECTED
        );
    }
    public List<JobApplicationEntity> myShortlisted(Long userAuthId) {
        return applicationRepo.findByUserAuthIdAndStatus(
                userAuthId,
                ApplicationStatus.SHORTLISTED
        );
    }
    
    
    
    //Change -------------> 
    
    public String generateResumeDownloadUrl(
            Long applicationId,
            Long recruiterAuthId
    ) {
        JobApplicationEntity app =
        		applicationRepo.findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException("Application not found")
                        );

        // 🔐 SECURITY: recruiter must own the job
        boolean ownsJob =
        		jobRepo.isJobOwnedByRecruiter(
                        app.getJobId(),
                        recruiterAuthId
                );

        if (!ownsJob) {
            throw new RuntimeException("Unauthorized access");
        }

        if (app.getResumeRef() == null) {
            throw new RuntimeException("Resume not uploaded");
        }

        // resume_ref → S3 key
        return resumeS3Service.generateDownloadUrl(
                app.getResumeRef()
        );

    }
    
    
    
    
    
    @Transactional
    public void startChatSession(
            Long jobId,
            Long candidateAuthId,
            Long recruiterAuthId,
            LocalDateTime requestedStartAt
    ) {
        // 1️⃣ Validate job ownership
        JobEntity job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiterAuthId().equals(recruiterAuthId)) {
            throw new RuntimeException("Unauthorized");
        }

        // 2️⃣ Validate candidate is shortlisted
        JobApplicationEntity application =
                applicationRepo.findByJobIdAndUserAuthId(jobId, candidateAuthId)
                        .orElseThrow(() -> new RuntimeException("Application not found"));

        if (application.getStatus() != ApplicationStatus.SHORTLISTED) {
            throw new IllegalStateException("Candidate not shortlisted");
        }

        // 3️⃣ Decide timing (SERVER AUTHORITATIVE)
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startAt = requestedStartAt != null
                ? requestedStartAt
                : now.plusMinutes(20);

        if (startAt.isBefore(now)) {
            throw new IllegalArgumentException("Chat start time cannot be in the past");
        }

        LocalDateTime endAt = startAt.plusMinutes(30);

        // 4️⃣ Generate chatId ONCE
        String chatId = "CHAT-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        // 5️⃣ Publish CHAT SESSION SCHEDULED event (STRUCTURED)
        JobEvent event = new JobEvent(
                "CHAT_SESSION_SCHEDULED",
                candidateAuthId,
                recruiterAuthId,
                jobId,
                chatId,           // ✅ chatId FIRST
                job.getTitle(),   // ✅ jobTitle SECOND
                startAt,
                endAt
        );

        jobEventProducer.publishToChat(event);
    }
}