package com.JobPortalJobService.Service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.JobPortalJobService.DTOS.ChatParticipantsResponse;
import com.JobPortalJobService.Entity.ApplicationStatus;
import com.JobPortalJobService.Entity.JobApplicationEntity;
import com.JobPortalJobService.Entity.JobEntity;

import com.JobPortalJobService.Repo.JobApplicationRepo;
import com.JobPortalJobService.Repo.JobPostingRepo;

@Service
public class ChatContextService {

    private final JobPostingRepo jobPostingRepo;
    private final JobApplicationRepo jobApplicationRepo;

    public ChatContextService(
            JobPostingRepo jobPostingRepo,
            JobApplicationRepo jobApplicationRepo
    ) {
        this.jobPostingRepo = jobPostingRepo;
        this.jobApplicationRepo = jobApplicationRepo;
    }

    public ChatParticipantsResponse resolveParticipants(
            Long jobId,
            Long requesterAuthId,
            Long candidateAuthId
    ) {

        JobEntity job = jobPostingRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Long recruiterAuthId = job.getRecruiterAuthId();

        JobApplicationEntity application;

        // 👨‍🎓 STUDENT FLOW
        if (candidateAuthId == null) {

            application = jobApplicationRepo
                    .findByJobIdAndUserAuthIdAndStatus(
                            jobId,
                            requesterAuthId,
                            ApplicationStatus.SHORTLISTED
                    )
                    .orElseThrow(() ->
                            new AccessDeniedException("You are not shortlisted for this job")
                    );

        }
        // 🧑‍💼 RECRUITER FLOW
        else {

            if (!requesterAuthId.equals(recruiterAuthId)) {
                throw new AccessDeniedException("Only recruiter can initiate this chat");
            }

            application = jobApplicationRepo
                    .findByJobIdAndUserAuthIdAndStatus(
                            jobId,
                            candidateAuthId,
                            ApplicationStatus.SHORTLISTED
                    )
                    .orElseThrow(() ->
                            new AccessDeniedException("Candidate is not shortlisted")
                    );
        }

        ChatParticipantsResponse response = new ChatParticipantsResponse();
        response.setJobId(jobId);
        response.setRecruiterId(recruiterAuthId);
        response.setCandidateId(application.getUserAuthId());

        return response;
    }
}
