package com.JobPortalUserService.DTOS;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRequest {
	

    // 🔹 profile fields
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String city;
    private Integer experience;
    private String university;
    private String education;
    private Boolean currentlyWorking;



}
