package com.JobPortalJobService.DTOS;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyJobRequest {

    // Future: URL or S3 key
    private String resumeRef;
}