package com.tecnocampus.LS2.protube_back.adapter.in.web;

import com.tecnocampus.LS2.protube_back.application.port.in.command.AuthenticateUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.command.RegisterUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.response.UserAuthResponse;
import com.tecnocampus.LS2.protube_back.application.port.in.usecase.AuthenticateUserUseCase;
import com.tecnocampus.LS2.protube_back.application.port.in.usecase.RegisterUserUseCase;
import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.AuthResponse;
import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private RegisterUserUseCase registerUserUseCase;

    @Mock
    private AuthenticateUserUseCase authenticateUserUseCase;

    @InjectMocks
    private AuthController authController;

    @Test
    void testRegisterUserSuccess() {
        RegisterUserCommand command = new RegisterUserCommand(
                "John",
                "Doe",
                "john@example.com",
                "johndoe",
                "password123"
        );

        UserAuthResponse authResponse = new UserAuthResponse(
                "jwt-token-123",
                1L,
                "johndoe",
                "john@example.com"
        );

        when(registerUserUseCase.registerUser(any(RegisterUserCommand.class))).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.register(command);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        AuthResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("jwt-token-123", body.getToken());
        assertEquals(1L, body.getUserId());
        assertEquals("johndoe", body.getUsername());
        assertEquals("john@example.com", body.getEmail());

        verify(registerUserUseCase).registerUser(any(RegisterUserCommand.class));
    }

    @Test
    void testLoginSuccess() {
        AuthenticateUserCommand command = new AuthenticateUserCommand("johndoe", "password123");

        UserAuthResponse authResponse = new UserAuthResponse(
                "jwt-token-123",
                1L,
                "johndoe",
                "john@example.com"
        );

        when(authenticateUserUseCase.authenticate(any(AuthenticateUserCommand.class))).thenReturn(authResponse);

        ResponseEntity<TokenResponse> response = authController.login(command);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        TokenResponse body = response.getBody();
        assertNotNull(body);

        verify(authenticateUserUseCase).authenticate(any(AuthenticateUserCommand.class));
    }

    @Test
    void testLoginWithValidCredentials() {
        AuthenticateUserCommand command = new AuthenticateUserCommand("alice", "secure123");

        UserAuthResponse authResponse = new UserAuthResponse(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                2L,
                "alice",
                "alice@example.com"
        );

        when(authenticateUserUseCase.authenticate(command)).thenReturn(authResponse);

        ResponseEntity<TokenResponse> response = authController.login(command);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testRegisterMultipleUsers() {
        RegisterUserCommand command1 = new RegisterUserCommand(
                "John",
                "Doe",
                "john@example.com",
                "johndoe",
                "password123"
        );

        RegisterUserCommand command2 = new RegisterUserCommand(
                "Jane",
                "Smith",
                "jane@example.com",
                "janesmith",
                "password456"
        );

        UserAuthResponse response1 = new UserAuthResponse("token1", 1L, "johndoe", "john@example.com");
        UserAuthResponse response2 = new UserAuthResponse("token2", 2L, "janesmith", "jane@example.com");

        when(registerUserUseCase.registerUser(any(RegisterUserCommand.class)))
                .thenReturn(response1)
                .thenReturn(response2);

        ResponseEntity<AuthResponse> result1 = authController.register(command1);
        ResponseEntity<AuthResponse> result2 = authController.register(command2);

        assertEquals(HttpStatus.CREATED, result1.getStatusCode());
        assertEquals(HttpStatus.CREATED, result2.getStatusCode());
        assertEquals("token1", result1.getBody().getToken());
        assertEquals("token2", result2.getBody().getToken());
    }

    @Test
    void testAuthResponseMapping() {
        RegisterUserCommand command = new RegisterUserCommand(
                "Bob",
                "Johnson",
                "bob@example.com",
                "bobjohnson",
                "pass123"
        );

        UserAuthResponse authResponse = new UserAuthResponse(
                "auth-token",
                3L,
                "bobjohnson",
                "bob@example.com"
        );

        when(registerUserUseCase.registerUser(any(RegisterUserCommand.class))).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.register(command);
        AuthResponse responseBody = response.getBody();

        assertNotNull(responseBody);
        assertEquals("auth-token", responseBody.getToken());
        assertEquals(3L, responseBody.getUserId());
        assertEquals("bobjohnson", responseBody.getUsername());
        assertEquals("bob@example.com", responseBody.getEmail());
    }
}
