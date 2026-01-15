package com.JobPortalJobService.DTOS;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateJobRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    @Size(max = 4000) // ~500 words
    private String description;

    @NotBlank
    private String location;

    @NotBlank
    private String employmentType; 
    // FULL_TIME, PART_TIME, INTERN, CONTRACT

    @NotNull
    @Min(0)
    @Max(50)
    private Integer experienceRequired;

    // ===== Salary =====
    @NotNull
    @Min(0)
    private Integer minSalary;

    @NotNull
    @Min(0)
    private Integer maxSalary;

    @NotBlank
    private String salaryCurrency;
}