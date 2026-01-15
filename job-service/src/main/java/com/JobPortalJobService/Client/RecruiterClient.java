package com.JobPortalJobService.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.JobPortalJobService.DTOS.CompanySnapshot;

@FeignClient(
        name = "jobportal-user-service",
        url = "http://localhost:8082"
)
public interface RecruiterClient {

    @GetMapping("/internal/recruiter/company-snapshot")

	CompanySnapshot getCompanySnapshot();
}
