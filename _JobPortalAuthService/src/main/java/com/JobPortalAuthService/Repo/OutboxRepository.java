package com.JobPortalAuthService.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JobPortalAuthService.Producer.OutboxEvent;


@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, String> {
	
	List<OutboxEvent> findTop20ByStatus(String status);

}
