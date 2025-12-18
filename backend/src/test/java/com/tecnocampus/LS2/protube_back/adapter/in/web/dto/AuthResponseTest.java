package com.tecnocampus.LS2.protube_back.adapter.in.web.dto;


import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void testAuthResponseCreation() {
        String token = "jwt-token-123";
        UUID userId = UUID.randomUUID();
        String username = "johndoe";
        String email = "john@example.com";

        AuthResponse response = new AuthResponse(token, userId, username, email);

        assertEquals(token, response.getToken());
        assertEquals(userId, response.getUserId());
        assertEquals(username, response.getUsername());
        assertEquals(email, response.getEmail());
    }

    @Test
    void testAuthResponseGetters() {
        AuthResponse response = new AuthResponse("token", UUID.randomUUID(), "alice", "alice@example.com");

        assertNotNull(response.getToken());
        assertNotNull(response.getUserId());
        assertNotNull(response.getUsername());
        assertNotNull(response.getEmail());
    }

    @Test
    void testAuthResponseInequality() {
        AuthResponse response1 = new AuthResponse("token1", UUID.randomUUID(), "user1", "email1@example.com");
        AuthResponse response2 = new AuthResponse("token2", UUID.randomUUID(), "user2", "email2@example.com");

        assertNotEquals(response1, response2);
    }

    @Test
    void testAuthResponseWithDifferentTokens() {
        UUID userId = UUID.randomUUID();
        AuthResponse response1 = new AuthResponse("token1", userId, "user", "email@example.com");
        AuthResponse response2 = new AuthResponse("token2", userId, "user", "email@example.com");

        assertNotEquals(response1, response2);
    }

    @Test
    void testAuthResponseWithDifferentUserIds() {
        AuthResponse response1 = new AuthResponse("token", UUID.randomUUID(), "user", "email@example.com");
        AuthResponse response2 = new AuthResponse("token", UUID.randomUUID(), "user", "email@example.com");

        assertNotEquals(response1, response2);
    }

    @Test
    void testAuthResponseWithDifferentUsernames() {
        UUID userId = UUID.randomUUID();
        AuthResponse response1 = new AuthResponse("token", userId, "user1", "email@example.com");
        AuthResponse response2 = new AuthResponse("token", userId, "user2", "email@example.com");

        assertNotEquals(response1, response2);
    }

    @Test
    void testAuthResponseWithDifferentEmails() {
        UUID userId = UUID.randomUUID();
        AuthResponse response1 = new AuthResponse("token", userId, "user", "email1@example.com");
        AuthResponse response2 = new AuthResponse("token", userId, "user", "email2@example.com");

        assertNotEquals(response1, response2);
    }

    @Test
    void testAuthResponseToString() {
        AuthResponse response = new AuthResponse("token", UUID.randomUUID(), "user", "email@example.com");

        String str = response.toString();
        assertNotNull(str);
        assertTrue(str.contains("AuthResponse") || str.contains("token"));
    }

    @Test
    void testAuthResponseWithNullValues() {
        AuthResponse response = new AuthResponse(null, null, null, null);

        assertNull(response.getToken());
        assertNull(response.getUserId());
        assertNull(response.getUsername());
        assertNull(response.getEmail());
    }
}
