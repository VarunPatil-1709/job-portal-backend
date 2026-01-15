package com.JobPortalAuthService.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JobPortalAuthService.Entity.AuthUser;

@Repository
public interface AuthRepo extends JpaRepository<AuthUser,Long> {
    Optional<AuthUser> findByEmail(String email);

    boolean existsByEmail(String email);

}
