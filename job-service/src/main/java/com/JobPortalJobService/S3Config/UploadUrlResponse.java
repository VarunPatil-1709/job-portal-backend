package com.JobPortalJobService.S3Config;

public record UploadUrlResponse(
        String uploadUrl,
        String resumeKey
) {}