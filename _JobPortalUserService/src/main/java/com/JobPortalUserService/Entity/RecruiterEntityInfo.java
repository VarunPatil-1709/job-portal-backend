package com.JobPortalUserService.Entity;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recruiter_info")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecruiterEntityInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long authId;

    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String role;

    // 🔹 company-related

    private String companyName;

    private String companyWebsite;
    private String companySize;
    private String companyIndustry;

    private Boolean verified = false;
    private Boolean profileCompleted = false;

}
