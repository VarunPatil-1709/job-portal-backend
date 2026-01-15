package com.JobPortalUserService.DTOS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanySnapshot {

    private String companyName;
    private String companySize;
    private String companyWebsite;
}
