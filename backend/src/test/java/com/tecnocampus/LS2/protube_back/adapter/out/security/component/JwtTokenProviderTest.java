package com.tecnocampus.LS2.protube_back.adapter.out.security.component;

import com.tecnocampus.LS2.protube_back.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private String jwtSecret = "myVerySecureSecretKeyForJWTTokenGenerationAndValidation12345";
    private int jwtExpirationMs = 86400000; // 24 hours

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    void testGenerateToken() {
        User user = new User(1L, "John", "Doe", "john@example.com", "johndoe", "hashed");

        String token = jwtTokenProvider.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGetUsernameFromToken() {
        User user = new User(1L, "John", "Doe", "john@example.com", "johndoe", "hashed");
        String token = jwtTokenProvider.generateToken(user);

        String username = jwtTokenProvider.getUsernameFromToken(token);

        assertEquals("johndoe", username);
    }

    @Test
    void testValidateTokenSuccess() {
        User user = new User(1L, "John", "Doe", "john@example.com", "johndoe", "hashed");
        String token = jwtTokenProvider.generateToken(user);

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void testValidateTokenInvalid() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void testValidateEmptyToken() {
        boolean isValid = jwtTokenProvider.validateToken("");

        assertFalse(isValid);
    }

    @Test
    void testGenerateTokenMultipleUsers() {
        User user1 = new User(1L, "User", "One", "user1@example.com", "user1", "hashed");
        User user2 = new User(2L, "User", "Two", "user2@example.com", "user2", "hashed");

        String token1 = jwtTokenProvider.generateToken(user1);
        String token2 = jwtTokenProvider.generateToken(user2);

        assertNotEquals(token1, token2);
        assertEquals("user1", jwtTokenProvider.getUsernameFromToken(token1));
        assertEquals("user2", jwtTokenProvider.getUsernameFromToken(token2));
    }

    @Test
    void testGetUsernameFromInvalidToken() {
        assertThrows(Exception.class, () -> jwtTokenProvider.getUsernameFromToken("invalid.token"));
    }

    @Test
    void testGenerateTokenIsNotEmpty() {
        User user = new User(1L, "John", "Doe", "john@example.com", "johndoe", "hashed");
        String token = jwtTokenProvider.generateToken(user);

        assertTrue(token.length() > 0);
        assertTrue(token.contains("."));
    }

    @Test
    void testTokenExpirationClaim() {
        User user = new User(1L, "John", "Doe", "john@example.com", "johndoe", "hashed");
        String token = jwtTokenProvider.generateToken(user);

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().getTime() > System.currentTimeMillis());
    }

    @Test
    void testValidateTokenWithMalformedFormat() {
        String malformedToken = "part1.part2"; // Only 2 parts instead of 3

        boolean isValid = jwtTokenProvider.validateToken(malformedToken);

        assertFalse(isValid);
    }
}
