package com.JobPortalUserService.DTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileStatusResponse {
    private Long authId;
    private String role;
    private boolean profileCompleted;
}