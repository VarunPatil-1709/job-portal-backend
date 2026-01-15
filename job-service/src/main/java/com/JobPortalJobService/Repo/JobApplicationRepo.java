package com.JobPortalJobService.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JobPortalJobService.DTOS.UserApplicationStats;
import com.JobPortalJobService.Entity.ApplicationStatus;
import com.JobPortalJobService.Entity.JobApplicationEntity;


@Repository
public interface JobApplicationRepo
        extends JpaRepository<JobApplicationEntity, Long> {

    boolean existsByJobIdAndUserAuthId(Long jobId, Long userAuthId);

    List<JobApplicationEntity> findByUserAuthId(Long userAuthId);

    List<JobApplicationEntity> findByJobId(Long jobId);
    
    Optional<JobApplicationEntity> 
    findByJobIdAndUserAuthIdAndStatus(
        Long jobId,
        Long userAuthId,
        ApplicationStatus status
    );

    Optional<JobApplicationEntity>
    findByJobIdAndStatus(
        Long jobId,
        ApplicationStatus status
    );

    

    Optional<JobApplicationEntity> findByJobIdAndUserAuthId(
            Long jobId,
            Long userAuthId
    );
    
    @Query(value = """
    	    SELECT
    	        COUNT(*) AS totalApplied,
    	        SUM(status = 'SHORTLISTED') AS shortlistedCount,
    	        SUM(status = 'REJECTED') AS rejectedCount,
    	        SUM(status = 'SELECTED') AS selectedCount
    	    FROM job_applications
    	    WHERE user_auth_id = :userAuthId
    	""", nativeQuery = true)
    	UserApplicationStats getUserApplicationStats(Long userAuthId);

    
    List<JobApplicationEntity> findByUserAuthIdAndStatus(
            Long userAuthId,
            ApplicationStatus status
    );

}