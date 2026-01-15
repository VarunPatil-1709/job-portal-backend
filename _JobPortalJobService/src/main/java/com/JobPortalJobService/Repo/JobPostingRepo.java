package com.JobPortalJobService.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.JobPortalJobService.Entity.JobEntity;

@Repository
public interface JobPostingRepo
        extends JpaRepository<JobEntity, Long> {

    List<JobEntity> findByActiveTrue();

    List<JobEntity> findByRecruiterAuthId(Long recruiterAuthId);
    
    
    
    Optional<JobEntity> findById(Long id);

        @Query("""
            SELECT COUNT(j) > 0
            FROM JobEntity j
            WHERE j.id = :jobId
              AND j.recruiterAuthId = :recruiterAuthId
        """)
        boolean isJobOwnedByRecruiter(
                @Param("jobId") Long jobId,
                @Param("recruiterAuthId") Long recruiterAuthId
        );
    }


