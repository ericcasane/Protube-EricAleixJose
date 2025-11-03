package com.tecnocampus.LS2.protube_back.application.service;

import com.tecnocampus.LS2.protube_back.adapter.out.in_memory.UserRepository;
import com.tecnocampus.LS2.protube_back.adapter.out.security.component.JwtTokenProvider;
import com.tecnocampus.LS2.protube_back.application.port.in.command.AuthenticateUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.response.UserAuthResponse;
import com.tecnocampus.LS2.protube_back.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private AuthenticateUserService service;

    @BeforeEach
    void setUp() {
        service = new AuthenticateUserService(userRepository, jwtTokenProvider);
    }

    @Test
    void testAuthenticateUserSuccess() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password123";
        String hashedPassword = encoder.encode(rawPassword);

        AuthenticateUserCommand command = new AuthenticateUserCommand("johndoe", rawPassword);
        User user = new User(1L, "John", "Doe", "john@example.com", "johndoe", hashedPassword);
        String token = "jwt-token-123";

        when(userRepository.findByUsername(command.username())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(user)).thenReturn(token);

        UserAuthResponse response = service.authenticate(command);

        assertNotNull(response);
        assertEquals(token, response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("johndoe", response.getUsername());
        assertEquals("john@example.com", response.getEmail());

        verify(userRepository).findByUsername(command.username());
        verify(jwtTokenProvider).generateToken(user);
    }

    @Test
    void testAuthenticateUserNotFound() {
        AuthenticateUserCommand command = new AuthenticateUserCommand("nonexistent", "password123");

        when(userRepository.findByUsername(command.username())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.authenticate(command));

        verify(userRepository).findByUsername(command.username());
        verify(jwtTokenProvider, never()).generateToken(any());
    }

    @Test
    void testAuthenticateUserInvalidPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("correctPassword");

        AuthenticateUserCommand command = new AuthenticateUserCommand("johndoe", "wrongPassword");
        User user = new User(1L, "John", "Doe", "john@example.com", "johndoe", hashedPassword);

        when(userRepository.findByUsername(command.username())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> service.authenticate(command));

        verify(userRepository).findByUsername(command.username());
        verify(jwtTokenProvider, never()).generateToken(any());
    }

    @Test
    void testAuthenticateUserWithEmptyPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("password123");

        AuthenticateUserCommand command = new AuthenticateUserCommand("johndoe", "");
        User user = new User(1L, "John", "Doe", "john@example.com", "johndoe", hashedPassword);

        when(userRepository.findByUsername(command.username())).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> service.authenticate(command));

        verify(userRepository).findByUsername(command.username());
        verify(jwtTokenProvider, never()).generateToken(any());
    }

    @Test
    void testAuthenticateUserTokenGeneration() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "mySecurePassword456";
        String hashedPassword = encoder.encode(rawPassword);

        AuthenticateUserCommand command = new AuthenticateUserCommand("alice", rawPassword);
        User user = new User(2L, "Alice", "Brown", "alice@example.com", "alice", hashedPassword);
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

        when(userRepository.findByUsername(command.username())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(user)).thenReturn(token);

        UserAuthResponse response = service.authenticate(command);

        assertEquals(token, response.getToken());
        assertEquals(2L, response.getUserId());
        assertEquals("alice", response.getUsername());
        assertEquals("alice@example.com", response.getEmail());

        verify(jwtTokenProvider).generateToken(user);
    }
}
