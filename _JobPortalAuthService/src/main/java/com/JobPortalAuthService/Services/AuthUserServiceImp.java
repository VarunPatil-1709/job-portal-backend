package com.JobPortalAuthService.Services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.JobPortalAuthService.DTOS.AuthUserRequest;
import com.JobPortalAuthService.DTOS.AuthUserResponse;
import com.JobPortalAuthService.DTOS.LoginRequest;
import com.JobPortalAuthService.Entity.AuthUser;
import com.JobPortalAuthService.Entity.RefreshToken;
import com.JobPortalAuthService.JwtUtil.JwtUtil;
import com.JobPortalAuthService.Producer.UserCreatedEvent;

import com.JobPortalAuthService.Repo.AuthRepo;
import com.JobPortalAuthService.Repo.RefreshTokenRepository;

import jakarta.transaction.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class AuthUserServiceImp implements AuthUserService {

    private final AuthRepo authUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    private static final int ACCESS_TOKEN_EXPIRY = 900; // 15 minutes
    private static final int REFRESH_TOKEN_DAYS = 7;

    public AuthUserServiceImp(
            AuthRepo authUserRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            OutboxService outboxService,
            ObjectMapper objectMapper ) {

        this.authUserRepository = authUserRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.outboxService = outboxService;
        this.objectMapper = objectMapper;
    }
    
/*    
	@Override
	@Transactional
	public void signup(AuthUserRequest request) {

	    if (authUserRepository.existsByEmail(request.getEmail())) {
	        throw new RuntimeException("Email already registered");
	    }

	    // 1️⃣ Save Auth User
	    AuthUser user = new AuthUser();
	    user.setEmail(request.getEmail());
	    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
	    user.setRole(request.getRole());

	    AuthUser savedUser = authUserRepository.save(user);

	    // 2️⃣ Build Kafka Event
	    // 2️⃣ Build Kafka Event (BUSINESS IDENTITY)
	    UserCreatedEvent event = new UserCreatedEvent(
	        "USER_CREATED_" + savedUser.getAuthId(),   // eventId ✅
	        "USER_CREATED",                            // eventType ✅
	        Instant.now(),                             // timestamp ✅
	        savedUser.getAuthId(),
	        savedUser.getEmail(),
	        savedUser.getRole().name()
	    );

	    // 3️⃣ Publish Kafka event
	    userEventProducer.sendUserCreatedEvent(event);
	}

    
    @Override
    @Transactional
    public void signup(AuthUserRequest request) {

        if (authUserRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // 1️⃣ Save Auth User
        AuthUser user = new AuthUser();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        AuthUser savedUser = authUserRepository.save(user);

        // 2️⃣ Build domain event (SAME as before)
        UserCreatedEvent event = new UserCreatedEvent(
            "USER_CREATED_" + savedUser.getAuthId(),
            "USER_CREATED",
            Instant.now(),
            savedUser.getAuthId(),
            savedUser.getEmail(),
            savedUser.getRole().name()
        );

        // 3️⃣ SAVE EVENT TO OUTBOX (THIS IS THE CHANGE)
        outboxService.saveEvent(
            event.getEventId(),
            event.getEventType(),
            new ObjectMapper().writeValueAsString(event)
        );
    }
*/
    
    @Override
    @Transactional
    public void signup(AuthUserRequest request) {

        if (authUserRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // 1️⃣ Save Auth User
        AuthUser user = new AuthUser();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        AuthUser savedUser = authUserRepository.save(user);

        // 2️⃣ Build domain event
        UserCreatedEvent event = new UserCreatedEvent(
                "USER_CREATED_" + savedUser.getAuthId(),
                "USER_CREATED",
                Instant.now(),
                savedUser.getAuthId(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );

        // 3️⃣ Serialize + save to outbox (SAFE)
        try {
            String payload = objectMapper.writeValueAsString(event);

            outboxService.saveEvent(
                    event.getEventId(),
                    event.getEventType(),
                    payload
            );

        } catch (Exception e) {
            // ❗ Important: fail transaction if serialization fails
            throw new RuntimeException("Failed to serialize UserCreatedEvent", e);
        }
    }
    
    
	@Override
	@Transactional
	public AuthUserResponse login(LoginRequest request) {

	    AuthUser user = authUserRepository.findByEmail(request.getEmail())
	            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

	    if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
	        throw new RuntimeException("Invalid credentials");
	    }

	    String accessToken =
	            jwtUtil.generateToken(user.getAuthId(), user.getRole().name());

	    String refreshToken = createOrUpdateRefreshToken(user.getAuthId());

	    return new AuthUserResponse(
	            accessToken,
	            refreshToken,
	            ACCESS_TOKEN_EXPIRY,
	            user.getRole()
	    );
	}

	
	

	@Override
	@Transactional
	public AuthUserResponse refresh(String refreshToken) {

	    RefreshToken token = refreshTokenRepository
	            .findByToken(refreshToken)
	            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

	    // 1️⃣ Check expiry
	    if (token.getExpiryDate().isBefore(Instant.now())) {
	        throw new RuntimeException("Refresh token expired");
	    }

	    // 2️⃣ Load user
	    AuthUser user = authUserRepository.findById(token.getAuthId())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // 3️⃣ Generate ONLY new access token
	    String newAccessToken =
	            jwtUtil.generateToken(user.getAuthId(), user.getRole().name());

	    // 🔴 DO NOT touch refresh token
	    return new AuthUserResponse(
	            newAccessToken,
	            refreshToken,              // SAME refresh token
	            ACCESS_TOKEN_EXPIRY,
	            user.getRole()
	    );
	}


	private String createOrUpdateRefreshToken(Long authId) {

	    RefreshToken token = refreshTokenRepository
	            .findByAuthId(authId)
	            .orElse(new RefreshToken());

	    token.setAuthId(authId);
	    token.setToken(UUID.randomUUID().toString());
	    token.setExpiryDate(
	            Instant.now().plus(REFRESH_TOKEN_DAYS, ChronoUnit.DAYS)
	    );

	    refreshTokenRepository.save(token);
	    return token.getToken();
	}




}
