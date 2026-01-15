package com.JobPortalUserService.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.JobPortalUserService.DTOS.CompanySnapshot;
import com.JobPortalUserService.DTOS.ProfileStatusResponse;
import com.JobPortalUserService.DTOS.RecruiterProfileRequest;
import com.JobPortalUserService.DTOS.RecruiterProfileResponse;
import com.JobPortalUserService.DTOS.StudentProfileResponse;
import com.JobPortalUserService.DTOS.UserProfileRequest;
import com.JobPortalUserService.Entity.RecruiterEntityInfo;
import com.JobPortalUserService.Entity.UserInfoEntity;
import com.JobPortalUserService.Repo.RecruiterInfoRepo;
import com.JobPortalUserService.Repo.UserInfoRepository;

@Service
public class UsersServicesImp implements UsersServices {

    private final UserInfoRepository userInfoRepository;
    private final RecruiterInfoRepo recruiterInfoRepo;

    public UsersServicesImp(UserInfoRepository userInfoRepository,
                            RecruiterInfoRepo recruiterInfoRepo) {
        this.userInfoRepository = userInfoRepository;
        this.recruiterInfoRepo = recruiterInfoRepo;
    }

    // ===============================
    // STUDENT PROFILE COMPLETE
    // ===============================
    @Transactional
    @Override
    public void userprofile(Long authId, UserProfileRequest request) {

        UserInfoEntity user = userInfoRepository.findByAuthId(authId)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        user.setGender(request.getGender());
        user.setCity(request.getCity());
        user.setExperience(request.getExperience());
        user.setUniversity(request.getUniversity());
        user.setEducation(request.getEducation());
        user.setCurrentlyWorking(request.getCurrentlyWorking());
        user.setProfileCompleted(true);

        userInfoRepository.save(user);
    }

    // ===============================
    // RECRUITER PROFILE COMPLETE
    // ===============================
    @Transactional
    @Override
    public void recruiterprofile(Long authId, RecruiterProfileRequest request) {

        RecruiterEntityInfo recruiter = recruiterInfoRepo.findByAuthId(authId)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));

        recruiter.setCompanyIndustry(request.getCompanyIndustry());
        recruiter.setCompanyName(request.getCompanyName());
        recruiter.setCompanySize(request.getCompanySize());
        recruiter.setCompanyWebsite(request.getCompanyWebsite());

        recruiter.setVerified(false);        // ✅ IMPORTANT FIX
        recruiter.setProfileCompleted(true);

        recruiterInfoRepo.save(recruiter);
    }

    // ===============================
    // INTERNAL SNAPSHOT (JOB SERVICE)
    // ===============================
    @Override
    public CompanySnapshot getRecruiterCompanySnapshot(Long authId) {

        RecruiterEntityInfo recruiter = recruiterInfoRepo.findByAuthId(authId)
                .orElseThrow(() ->
                        new RuntimeException("Recruiter profile not found"));

        if (recruiter.getCompanyName() == null) {
            throw new RuntimeException("Recruiter company profile incomplete");
        }

        return new CompanySnapshot(
                recruiter.getCompanyName(),
                recruiter.getCompanySize(),
                recruiter.getCompanyWebsite()
        );
    }

    // ===============================
    // PROFILE STATUS
    // ===============================
    @Override
    public ProfileStatusResponse getProfileStatus(Long authId, String role) {

        if ("STUDENT".equals(role)) {
            return userInfoRepository.findByAuthId(authId)
                    .map(user -> new ProfileStatusResponse(
                            authId,
                            role,
                            Boolean.TRUE.equals(user.getProfileCompleted())
                    ))
                    .orElse(new ProfileStatusResponse(authId, role, false));
        }

        if ("RECRUITER".equals(role)) {
            return recruiterInfoRepo.findByAuthId(authId)
                    .map(rec -> new ProfileStatusResponse(
                            authId,
                            role,
                            Boolean.TRUE.equals(rec.getProfileCompleted())
                    ))
                    .orElse(new ProfileStatusResponse(authId, role, false));
        }

        throw new RuntimeException("Invalid role");
    }

    // ===============================
    // GET FULL PROFILE (ME)
    // ===============================
    @Override
    public Object getMyProfile(Long authId, String role) {

        if ("STUDENT".equals(role)) {

            UserInfoEntity user = userInfoRepository.findByAuthId(authId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            StudentProfileResponse res = new StudentProfileResponse();
            res.setAuthId(authId);
            res.setRole("STUDENT");

            res.setEmail(user.getEmail());
            res.setProfileCompleted(user.getProfileCompleted());

            res.setFirstName(user.getFirstName());
            res.setLastName(user.getLastName());
            res.setDob(user.getDob());
            res.setGender(user.getGender());
            res.setCity(user.getCity());
            res.setExperience(user.getExperience());
            res.setUniversity(user.getUniversity());
            res.setEducation(user.getEducation());
            res.setCurrentlyWorking(user.getCurrentlyWorking());

            return res;
        }

        if ("RECRUITER".equals(role)) {

            RecruiterEntityInfo recruiter = recruiterInfoRepo.findByAuthId(authId)
                    .orElseThrow(() -> new RuntimeException("Recruiter not found"));

            RecruiterProfileResponse res = new RecruiterProfileResponse();
            res.setAuthId(authId);
            res.setRole("RECRUITER");

            res.setEmail(recruiter.getEmail());
            res.setProfileCompleted(recruiter.getProfileCompleted());

            res.setCompanyName(recruiter.getCompanyName());
            res.setCompanyWebsite(recruiter.getCompanyWebsite());
            res.setCompanySize(recruiter.getCompanySize());
            res.setCompanyIndustry(recruiter.getCompanyIndustry());
            res.setVerified(recruiter.getVerified());

            return res;
        }

        throw new RuntimeException("Invalid role");
    }
}
