package com.JobPortalUserService.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JobPortalUserService.Entity.RecruiterEntityInfo;
import com.JobPortalUserService.Entity.UserInfoEntity;

@Repository
public interface RecruiterInfoRepo extends JpaRepository<RecruiterEntityInfo, Long> {
    Optional<RecruiterEntityInfo> findByAuthId(Long authId);

    boolean existsByAuthId(Long authId);

}
