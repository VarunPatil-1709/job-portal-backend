package com.JobPortalAuthService.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JobPortalAuthService.Entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
	
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByAuthId(Long authId);

    void deleteByAuthId(Long authId);
    
    

}
