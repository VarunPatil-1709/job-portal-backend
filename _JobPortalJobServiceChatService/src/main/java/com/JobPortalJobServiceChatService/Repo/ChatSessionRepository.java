package com.JobPortalJobServiceChatService.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JobPortalJobServiceChatService.Entity.ChatSession;

@Repository

public interface ChatSessionRepository extends JpaRepository<ChatSession, String> {
	 @Query("""
		        SELECT c FROM ChatSession c
		        WHERE (c.candidateId = :userId OR c.recruiterId = :userId)
		        AND (:jobId IS NULL OR c.jobId = :jobId)
		        AND (
		            :type = 'ALL'
		            OR (:type = 'ACTIVE' AND c.status = 'ACTIVE')
		            OR (:type = 'UPCOMING' AND c.status = 'CREATED' AND c.startAt > CURRENT_TIMESTAMP)
		        )
		        ORDER BY c.startAt ASC
		    """)
		    List<ChatSession> findUserChats(
		            Long userId,
		            String type,
		            Long jobId
		    );
}