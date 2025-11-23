package com.tecnocampus.LS2.protube_back.adapter.out.security.component;

import com.tecnocampus.LS2.protube_back.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt.secret:myVerySecureSecretKeyForJWTTokenGenerationAndValidation12345}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")
    private int jwtExpirationMs;

    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .subject(user.username())
                .claim("userId", user.id())
                .claim("email", user.email())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getTokenBody(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getTokenBody(token);
        Object userIdClaim = claims.get("userId");
        if (userIdClaim instanceof Integer) {
            return ((Integer) userIdClaim).longValue();
        } else if (userIdClaim instanceof Long) {
            return (Long) userIdClaim;
        }
        throw new IllegalArgumentException("Invalid userId in token");
    }

    public boolean validateToken(String token) {
        try {
            getTokenBody(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getTokenBody(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
