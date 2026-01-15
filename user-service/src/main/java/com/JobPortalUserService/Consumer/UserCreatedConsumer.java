package com.JobPortalUserService.Consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.JobPortalUserService.Entity.RecruiterEntityInfo;
import com.JobPortalUserService.Entity.UserInfoEntity;
import com.JobPortalUserService.Repo.RecruiterInfoRepo;
import com.JobPortalUserService.Repo.UserInfoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;


@Service
public class UserCreatedConsumer {

    private final UserInfoRepository userRepo;
    private final RecruiterInfoRepo recruiterRepo;
    private final ProcessedEventRepository processedEventRepo;

    public UserCreatedConsumer(UserInfoRepository userRepo,
                               RecruiterInfoRepo recruiterRepo,
                               ProcessedEventRepository processedEventRepo) {
        this.userRepo = userRepo;
        this.recruiterRepo = recruiterRepo;
        this.processedEventRepo = processedEventRepo;
    }

    @KafkaListener(
        topics = "user-created-topic",
        groupId = "user-service-group"
    )
    @Transactional
    public void consume(UserCreatedEvent event) {

        // 🔑 1. Kafka idempotency check
        if (processedEventRepo.existsById(event.getEventId())) {
            return; // duplicate → safe skip
        }

        // 2. Business logic
        if ("RECRUITER".equalsIgnoreCase(event.getRole())) {
            handleRecruiter(event);
        } else {
            handleUser(event);
        }

        // 🔑 3. Mark event as processed
        processedEventRepo.save(new ProcessedEvent(event.getEventId()));
    }

    private void handleUser(UserCreatedEvent event) {
        if (userRepo.existsByAuthId(event.getAuthId())) return;

        UserInfoEntity user = new UserInfoEntity();
        user.setAuthId(event.getAuthId());
        user.setEmail(event.getEmail());
        user.setRole(event.getRole());
        user.setProfileCompleted(false);

        userRepo.save(user);
    }

    private void handleRecruiter(UserCreatedEvent event) {
        if (recruiterRepo.existsByAuthId(event.getAuthId())) return;

        RecruiterEntityInfo recruiter = new RecruiterEntityInfo();
        recruiter.setAuthId(event.getAuthId());
        recruiter.setEmail(event.getEmail());
        recruiter.setRole(event.getRole());
        recruiter.setProfileCompleted(false);

        recruiterRepo.save(recruiter);
    }
}/*
@Service
public class UserCreatedConsumer {

    private final UserInfoRepository userRepo;
    private final RecruiterInfoRepo recruiterRepo;
    private final ProcessedEventRepository processedEventRepo;
    private final ObjectMapper objectMapper;

    public UserCreatedConsumer(UserInfoRepository userRepo,
                               RecruiterInfoRepo recruiterRepo,
                               ProcessedEventRepository processedEventRepo,
                               ObjectMapper objectMapper) {
        this.userRepo = userRepo;
        this.recruiterRepo = recruiterRepo;
        this.processedEventRepo = processedEventRepo;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
        topics = "user-created-topic",
        groupId = "user-service-group"
    )
    @Transactional
    public void consume(String message) {

        try {
            // 🔥 Deserialize manually
            UserCreatedEvent event =
                    objectMapper.readValue(message, UserCreatedEvent.class);

            // 🔑 Idempotency check
            if (processedEventRepo.existsById(event.getEventId())) {
                return;
            }

            // Business logic
            if ("RECRUITER".equalsIgnoreCase(event.getRole())) {
                handleRecruiter(event);
            } else {
                handleUser(event);
            }

            // Mark event processed
            processedEventRepo.save(new ProcessedEvent(event.getEventId()));

        } catch (Exception e) {
            // ❗ Fail fast → Kafka will retry
            throw new RuntimeException("Failed to process user-created event", e);
        }
    }

    private void handleUser(UserCreatedEvent event) {
        if (userRepo.existsByAuthId(event.getAuthId())) return;

        UserInfoEntity user = new UserInfoEntity();
        user.setAuthId(event.getAuthId());
        user.setEmail(event.getEmail());
        user.setRole(event.getRole());
        user.setProfileCompleted(false);

        userRepo.save(user);
    }

    private void handleRecruiter(UserCreatedEvent event) {
        if (recruiterRepo.existsByAuthId(event.getAuthId())) return;

        RecruiterEntityInfo recruiter = new RecruiterEntityInfo();
        recruiter.setAuthId(event.getAuthId());
        recruiter.setEmail(event.getEmail());
        recruiter.setRole(event.getRole());
        recruiter.setProfileCompleted(false);

        recruiterRepo.save(recruiter);
    }
}*/
