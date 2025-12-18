package com.tecnocampus.LS2.protube_back.application.service;

import com.tecnocampus.LS2.protube_back.adapter.out.in_memory.UserRepository;
import com.tecnocampus.LS2.protube_back.adapter.out.security.component.JwtTokenProvider;
import com.tecnocampus.LS2.protube_back.application.port.in.command.RegisterUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.response.UserAuthResponse;
import com.tecnocampus.LS2.protube_back.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private RegisterUserService service;

    @BeforeEach
    void setUp() {
        service = new RegisterUserService(userRepository, jwtTokenProvider);
    }

    @Test
    void testRegisterUserSuccess() {
        RegisterUserCommand command = new RegisterUserCommand(
                "John",
                "Doe",
                "john@example.com",
                "johndoe",
                "password123",
                "Description"
        );

        User newUser = User.from(command);
        User savedUser = User.from(UUID.randomUUID(), newUser.name(), newUser.surname(), newUser.email(), newUser.username(), newUser.hashedPassword(), newUser.description(), null, null);
        String token = "jwt-token-123";

        when(userRepository.findByEmail(command.email())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(command.username())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtTokenProvider.generateToken(savedUser)).thenReturn(token);

        UserAuthResponse response = service.registerUser(command);

        assertNotNull(response);
        assertEquals(token, response.getToken());
        assertEquals(savedUser.id(), response.getUserId());
        assertEquals("johndoe", response.getUsername());
        assertEquals("john@example.com", response.getEmail());

        verify(userRepository).findByEmail(command.email());
        verify(userRepository).findByUsername(command.username());
        verify(userRepository).save(any(User.class));
        verify(jwtTokenProvider).generateToken(savedUser);
    }

    @Test
    void testRegisterUserEmailAlreadyExists() {
        RegisterUserCommand command = new RegisterUserCommand(
                "John",
                "Doe",
                "john@example.com",
                "johndoe",
                "password123",
                "Description"
        );

        User existingUser = User.from(UUID.randomUUID(), "Jane", "Smith", "john@example.com", "janesmith", "hashed", null, null, null);

        when(userRepository.findByEmail(command.email())).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> service.registerUser(command));

        verify(userRepository).findByEmail(command.email());
        verify(userRepository, never()).findByUsername(any());
        verify(userRepository, never()).save(any());
        verify(jwtTokenProvider, never()).generateToken(any());
    }

    @Test
    void testRegisterUserUsernameAlreadyExists() {
        RegisterUserCommand command = new RegisterUserCommand(
                "John",
                "Doe",
                "john@example.com",
                "johndoe",
                "password123",
                "Description"
        );

        User existingUser = User.from(UUID.randomUUID(), "Jane", "Smith", "jane@example.com", "johndoe", "hashed", null, null, null);

        when(userRepository.findByEmail(command.email())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(command.username())).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> service.registerUser(command));

        verify(userRepository).findByEmail(command.email());
        verify(userRepository).findByUsername(command.username());
        verify(userRepository, never()).save(any());
        verify(jwtTokenProvider, never()).generateToken(any());
    }

    @Test
    void testRegisterUserWithSpecialCharacters() {
        RegisterUserCommand command = new RegisterUserCommand(
                "José",
                "García",
                "jose@example.com",
                "jose_garcia",
                "p@ssw0rd",
                "Descripción"
        );

        User newUser = User.from(command);
        User savedUser = User.from(UUID.randomUUID(), newUser.name(), newUser.surname(), newUser.email(), newUser.username(), newUser.hashedPassword(), newUser.description(), null, null);
        String token = "jwt-token-456";

        when(userRepository.findByEmail(command.email())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(command.username())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtTokenProvider.generateToken(savedUser)).thenReturn(token);

        UserAuthResponse response = service.registerUser(command);

        assertNotNull(response);
        assertEquals(token, response.getToken());
        verify(userRepository).findByEmail(command.email());
        verify(userRepository).findByUsername(command.username());
    }
}
