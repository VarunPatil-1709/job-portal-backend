package com.JobPortalJobService.S3Config;

import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class ResumeS3Service {

    private static final String BUCKET = "job-portal-resumes-prod";

    private final S3Presigner presigner;

    public ResumeS3Service(S3Presigner presigner) {
        this.presigner = presigner;
    }

    public UploadUrlResponse generateUploadUrl(Long userAuthId, String fileName) {

        String key = "resumes/" + userAuthId + "/" +
                UUID.randomUUID() + "-" + fileName;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(key)
                .contentType("application/pdf")
                .build();

        PutObjectPresignRequest presignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .putObjectRequest(request)
                        .build();

        String uploadUrl =
                presigner.presignPutObject(presignRequest).url().toString();

        return new UploadUrlResponse(uploadUrl, key);
    }
    
    public String generateDownloadUrl(String resumeRef) {

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket("job-portal-resumes-prod")
                .key(resumeRef)
                .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(request)
                        .build();

        return presigner.presignGetObject(presignRequest)
                .url()
                .toString();
    }
    
    

}
