package com.JobPortalUserService.DTOS;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterProfileRequest {
	

    private String companyName;
    private String companyWebsite;
    private String companySize;
    private String companyIndustry;
    private Boolean verified = false;


}
