package com.tecnocampus.LS2.protube_back.adapter.in.web.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenResponseTest {

    @Test
    void testTokenResponseCreation() {
        String token = "jwt-token-123";
        TokenResponse response = new TokenResponse(token);

        assertEquals(token, response.token());
    }

    @Test
    void testTokenResponseEquality() {
        String token = "jwt-token-123";
        TokenResponse response1 = new TokenResponse(token);
        TokenResponse response2 = new TokenResponse(token);

        assertEquals(response1, response2);
    }

    @Test
    void testTokenResponseInequality() {
        TokenResponse response1 = new TokenResponse("token1");
        TokenResponse response2 = new TokenResponse("token2");

        assertNotEquals(response1, response2);
    }

    @Test
    void testTokenResponseToString() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        TokenResponse response = new TokenResponse(token);

        String str = response.toString();
        assertNotNull(str);
        assertTrue(str.contains("TokenResponse"));
    }

    @Test
    void testTokenResponseWithLongToken() {
        String longToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huZG9lIiwidXNlcklkIjoxLCJlbWFpbCI6ImpvaG5AZXhhbXBsZS5jb20ifQ.signature";
        TokenResponse response = new TokenResponse(longToken);

        assertEquals(longToken, response.token());
    }

    @Test
    void testTokenResponseWithEmptyToken() {
        TokenResponse response = new TokenResponse("");

        assertEquals("", response.token());
    }

    @Test
    void testTokenResponseWithNullToken() {
        TokenResponse response = new TokenResponse(null);

        assertNull(response.token());
    }

    @Test
    void testTokenResponseRecord() {
        String token = "test-token";
        TokenResponse response1 = new TokenResponse(token);
        TokenResponse response2 = response1;

        assertSame(response1, response2);
    }
}
