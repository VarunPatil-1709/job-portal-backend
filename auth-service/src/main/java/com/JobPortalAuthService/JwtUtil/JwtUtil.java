package com.JobPortalAuthService.JwtUtil;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

    // MUST be Base64 encoded (minimum 32 bytes)
    @Value("${jwt.secret}")
    private String secret;

    private final long EXPIRATION = 1000 * 60 * 15; // 15 minutes

    /* ===================== TOKEN GENERATION ===================== */

    public String generateToken(Long userId, String role) {

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey())   // ✅ correct way
                .compact();
    }

    /* ===================== TOKEN VALIDATION ===================== */

    public Claims validateToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)    // ✅ validates signature & expiry
                .getBody();
    }

    /* ===================== CLAIM EXTRACTION ===================== */

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = validateToken(token);
        return resolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /* ===================== SIGNING KEY ===================== */

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
