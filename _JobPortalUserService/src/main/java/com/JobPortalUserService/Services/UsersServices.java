package com.JobPortalUserService.Services;

import com.JobPortalUserService.DTOS.UserProfileRequest;
import com.JobPortalUserService.DTOS.CompanySnapshot;
import com.JobPortalUserService.DTOS.ProfileStatusResponse;
import com.JobPortalUserService.DTOS.RecruiterProfileRequest;

public interface UsersServices {
	
	void userprofile(Long authId,UserProfileRequest request);
	void recruiterprofile(Long authId,RecruiterProfileRequest request);
	CompanySnapshot getRecruiterCompanySnapshot(Long authId);
	
	ProfileStatusResponse getProfileStatus(Long authId, String role);
	
	Object getMyProfile(Long authId, String role);

	

}
