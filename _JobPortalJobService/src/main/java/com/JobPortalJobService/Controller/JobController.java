package com.JobPortalJobService.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.JobPortalJobService.DTOS.CreateJobRequest;
import com.JobPortalJobService.Entity.JobEntity;
import com.JobPortalJobService.Service.JobService;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

	
    private final JobService jobService;

    /**
     * ===============================
     * POST JOB (Recruiter only)
     * ===============================
     */
    @PostMapping
    public ResponseEntity<?> createJob(
            @Valid @RequestBody CreateJobRequest request
    ) {
        jobService.createJob(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Job posted successfully");
    }

    /**
     * ===============================
     * GET ALL ACTIVE JOBS (Public)
     * ===============================
     */
    @GetMapping
    public ResponseEntity<List<JobEntity>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllActiveJobs());
    }

    /**
     * ===============================
     * GET JOB BY ID (Public)
     * ===============================
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobEntity> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    /**
     * ===============================
     * GET RECRUITER'S JOBS (Protected)
     * ===============================
     */
    @GetMapping("/my")
    public ResponseEntity<List<JobEntity>> getMyJobs() {
        return ResponseEntity.ok(
                jobService.getJobsByRecruiter()
        );
    }
}
