package com.JobPortalJobService.Config;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Claims getClaims(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);

        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Long extractUserAuthId(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    public String extractSubject(String token) {
        return getClaims(token).getSubject(); // authId as String
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }
}
