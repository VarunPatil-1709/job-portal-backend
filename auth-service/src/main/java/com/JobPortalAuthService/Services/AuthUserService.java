package com.JobPortalAuthService.Services;

import org.springframework.stereotype.Service;

import com.JobPortalAuthService.DTOS.AuthUserRequest;
import com.JobPortalAuthService.DTOS.AuthUserResponse;
import com.JobPortalAuthService.DTOS.LoginRequest;
import com.JobPortalAuthService.DTOS.refreshTokenRequest;
@Service
public interface AuthUserService {
	void signup(AuthUserRequest request);

    AuthUserResponse login(LoginRequest request);

    AuthUserResponse refresh(String request);

}
