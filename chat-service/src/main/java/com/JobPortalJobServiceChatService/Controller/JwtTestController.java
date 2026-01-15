package com.JobPortalJobServiceChatService.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JobPortalJobServiceChatService.Config.JwtUtil;

@RestController
@RequestMapping("/test")
public class JwtTestController {

    private final JwtUtil jwtUtil;

    public JwtTestController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/jwt")
    public Map<String, Object> testJwt(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", jwtUtil.isTokenValid(token));
        response.put("userId", jwtUtil.extractUserId(token));
        response.put("role", jwtUtil.extractRole(token));

        return response;
    }
}
