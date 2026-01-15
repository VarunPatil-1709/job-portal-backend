package com.JobPortalJobService.S3Config;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(
	    origins = "http://localhost:5173",
	    allowCredentials = "true"
	)
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeS3Service resumeS3Service;

    public ResumeController(ResumeS3Service resumeS3Service) {
        this.resumeS3Service = resumeS3Service;
    }

    @PostMapping("/upload-url")
    public UploadUrlResponse generateUploadUrl(@RequestParam String fileName) {

        var authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated request");
        }

        Long userAuthId =
                Long.parseLong(authentication.getPrincipal().toString());

        return resumeS3Service.generateUploadUrl(userAuthId, fileName);
    }
    
    @GetMapping("/download")
    public ResponseEntity<?> downloadResume(
            @RequestParam String resumeRef
    ) {
        String downloadUrl =
                resumeS3Service.generateDownloadUrl(resumeRef);

        return ResponseEntity.ok(
                Map.of("downloadUrl", downloadUrl)
        );
    }

}
