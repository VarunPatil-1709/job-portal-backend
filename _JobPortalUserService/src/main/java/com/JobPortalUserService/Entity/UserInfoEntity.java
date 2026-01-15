package com.JobPortalUserService.Entity;

import java.time.LocalDate;

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
@Table(name = "users_info")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long authId;

    // 🔹 replicated from Auth Service
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role;

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

    private Boolean profileCompleted = false;

}
