package com.ecommerce.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    
    @Value("${app.jwt.secret:}")
    private String jwtSecret;
    
    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpirationMs;
    
    public String generateToken(Authentication authentication) {
        String userEmail = authentication.getName();
        Instant now = Instant.now();
        Instant expiryDate = now.plusMillis(jwtExpirationMs);
        
        SecretKey key = Keys.hmacShaKeyFor(getJwtSecret().getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .subject(userEmail)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiryDate))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    
    public String generateTokenFromEmail(String email) {
        Instant now = Instant.now();
        Instant expiryDate = now.plusMillis(jwtExpirationMs);
        
        SecretKey key = Keys.hmacShaKeyFor(getJwtSecret().getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiryDate))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    
    public String getUserEmailFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(getJwtSecret().getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(getJwtSecret().getBytes(StandardCharsets.UTF_8));
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }
    
    private String getJwtSecret() {
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            return "my-super-secret-key-that-is-at-least-256-bits-long-for-hs512-algorithm";
        }
        return jwtSecret;
    }
}
