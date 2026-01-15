package com.JobPortalJobService.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.JobPortalJobService.DTOS.ApplyJobRequest;
import com.JobPortalJobService.DTOS.ChatSessionRequest;
import com.JobPortalJobService.DTOS.ChatSessionResponse;
import com.JobPortalJobService.DTOS.UserApplicationStats;
import com.JobPortalJobService.Entity.ApplicationStatus;
import com.JobPortalJobService.Entity.JobApplicationEntity;
import com.JobPortalJobService.Service.JobApplicationService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService applicationService;

    @PostMapping("/jobs/{jobId}/apply")
    public ResponseEntity<?> applyToJob(
            @PathVariable Long jobId,
            @RequestBody(required = false) ApplyJobRequest request,
            Authentication authentication
    ) {

        // ✅ principal is STRING
        String authIdStr = authentication.getPrincipal().toString();
        Long userAuthId = Long.valueOf(authIdStr);

        applicationService.applyToJob(
                jobId,
                userAuthId,
                request != null ? request.getResumeRef() : null
        );

        return ResponseEntity.ok("Applied successfully");
    }

    // ===============================
    // STUDENT: MY APPLICATIONS
    // ===============================
    @GetMapping("/applications/my")
    public ResponseEntity<List<JobApplicationEntity>> myApplications(
            Authentication authentication
    ) {
        // ✅ JWT always gives String
        Long userAuthId = Long.parseLong(authentication.getName());

        return ResponseEntity.ok(
                applicationService.getMyApplications(userAuthId)
        );
    }

    // ===============================
    // RECRUITER: VIEW APPLICANTS
    // ===============================
    @GetMapping("/jobs/{jobId}/applications")
    public ResponseEntity<List<JobApplicationEntity>> jobApplicants(
            @PathVariable Long jobId
    ) {
        return ResponseEntity.ok(
                applicationService.getApplicantsForJob(jobId)
        );
    }
    
    @PatchMapping("/application/status")
    public ResponseEntity<String> updateApplicationStatus(
            @RequestParam Long jobId,
            @RequestParam Long userAuthId,
            @RequestParam ApplicationStatus status
    ) {
    	applicationService.updateApplicationStatus(
                userAuthId,
                jobId,
                status
        );

        return ResponseEntity.ok("Application status updated successfully");
    }
    
    
    @GetMapping("/stats")
    public ResponseEntity<UserApplicationStats> getUserStats(
            Authentication authentication
    ) {
        // ✅ NO request param
        Long userAuthId = Long.parseLong(authentication.getName());

        return ResponseEntity.ok(
                applicationService.getStats(userAuthId)
        );
    }
    
    @GetMapping("/my/job/applied")
    public ResponseEntity<List<JobApplicationEntity>> myapplied(
            Authentication authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        Long userAuthId = Long.parseLong(authentication.getName());

        return ResponseEntity.ok(
                applicationService.myApplied(userAuthId)
        );
    }
    
    @GetMapping("/my/job/rejected")
    public ResponseEntity<List<JobApplicationEntity>> myrejected(
            Authentication authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        Long userAuthId = Long.parseLong(authentication.getName());

        return ResponseEntity.ok(
                applicationService.myRejected(userAuthId)
        );
    }
    
    @GetMapping("/my/job/shortlisted")
    public ResponseEntity<List<JobApplicationEntity>> myshortlisted(
            Authentication authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        Long userAuthId = Long.parseLong(authentication.getName());

        return ResponseEntity.ok(
                applicationService.myShortlisted(userAuthId)
        );
    }
    
    
    
    @GetMapping("/applications/{applicationId}/resume")
    public ResponseEntity<?> downloadResume(
            @PathVariable Long applicationId,
            Authentication authentication
    ) {
        // recruiter auth id from JWT
        Long recruiterAuthId = Long.parseLong(authentication.getName());

        String downloadUrl =
                applicationService.generateResumeDownloadUrl(
                        applicationId,
                        recruiterAuthId
                );

        return ResponseEntity.ok(
                java.util.Map.of("downloadUrl", downloadUrl)
        );
    }
    
    @PostMapping("/jobs/{jobId}/chat-session")
    public ResponseEntity<Void> startChat(
            @PathVariable Long jobId,
            @RequestBody ChatSessionRequest request,
            Principal principal
    ) {
        Long recruiterAuthId = Long.parseLong(principal.getName());

        applicationService.startChatSession(
                jobId,
                request.getCandidateAuthId(),
                recruiterAuthId,
                request.getStartAt()   // ✅ pass optional start time
        );

        // ✅ Async, Kafka-driven
        return ResponseEntity.accepted().build();
    }



    
    

    
    

}
