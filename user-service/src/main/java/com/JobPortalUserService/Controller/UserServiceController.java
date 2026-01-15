package com.JobPortalUserService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.JobPortalUserService.DTOS.CompanySnapshot;
import com.JobPortalUserService.DTOS.ProfileStatusResponse;
import com.JobPortalUserService.DTOS.RecruiterProfileRequest;
import com.JobPortalUserService.DTOS.UserProfileRequest;
import com.JobPortalUserService.Services.UsersServices;

@RestController
@RequestMapping
public class UserServiceController {

    @Autowired
    private UsersServices usersServices;

    // ===============================
    // USER PROFILE
    // ===============================
    @PostMapping("/profile/user")
    public String completeUserProfile(
            @RequestBody UserProfileRequest request,
            Authentication authentication) {

        Long authId = Long.valueOf(authentication.getName());
        usersServices.userprofile(authId, request);

        return "Profile completed";
    }

    @PostMapping("/profile/recruiter")
    public String completeRecruiterProfile(
            @RequestBody RecruiterProfileRequest request,
            Authentication authentication) {

        Long authId = Long.valueOf(authentication.getName());
        usersServices.recruiterprofile(authId, request);

        return "Profile completed";
    }

    // ===============================
    // 🔐 INTERNAL SNAPSHOT (FIXED)
    // ===============================
    @GetMapping("/internal/recruiter/company-snapshot")
    public CompanySnapshot getCompanySnapshot(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Unauthenticated internal request");
        }

        Long recruiterAuthId = Long.valueOf(authentication.getName());

        return usersServices.getRecruiterCompanySnapshot(recruiterAuthId);
    }

    // ===============================
    // PROFILE STATUS
    // ===============================
    @GetMapping("/profile/status")
    public ProfileStatusResponse getProfileStatus(Authentication authentication) {

        Long authId = Long.valueOf(authentication.getName());

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        return usersServices.getProfileStatus(authId, role);
    }

    // ===============================
    // MY PROFILE
    // ===============================
    @GetMapping("/profile/me")
    public Object getMyProfile(Authentication authentication) {

        Long authId = Long.valueOf(authentication.getName());

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        return usersServices.getMyProfile(authId, role);
    }
}
