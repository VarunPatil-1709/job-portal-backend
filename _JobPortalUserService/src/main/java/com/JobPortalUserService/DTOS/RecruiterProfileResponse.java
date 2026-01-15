package com.JobPortalUserService.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterProfileResponse {

    private Long authId;
    private String role;

    private String email;
    private Boolean profileCompleted;

    private String companyName;
    private String companyWebsite;
    private String companySize;
    private String companyIndustry;
    private Boolean verified;
}
