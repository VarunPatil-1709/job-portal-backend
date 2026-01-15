package com.JobPortalUserService.DTOS;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentProfileResponse {

    private Long authId;
    private String role;

    private String email;
    private Boolean profileCompleted;

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
