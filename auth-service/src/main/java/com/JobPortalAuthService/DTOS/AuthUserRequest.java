package com.JobPortalAuthService.DTOS;

import com.JobPortalAuthService.Entity.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AuthUserRequest {
	
	private String email;
	
	private String password;
	
	private Role role;

}
