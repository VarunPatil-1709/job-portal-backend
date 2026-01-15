package com.JobPortalAuthService.DTOS;

import com.JobPortalAuthService.Entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class AuthUserResponse {
	
    private String accessToken;
    
    private String refreshToken;
    
    private int expiresIn;
    
    private Role role;

}
