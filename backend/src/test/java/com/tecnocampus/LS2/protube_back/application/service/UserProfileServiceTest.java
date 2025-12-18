package com.tecnocampus.LS2.protube_back.application.service;

import com.tecnocampus.LS2.protube_back.adapter.out.in_memory.UserRepository;
import com.tecnocampus.LS2.protube_back.application.port.in.command.UpdateUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.response.UserProfileResponse;
import com.tecnocampus.LS2.protube_back.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserProfileServiceTest {

    private UserProfileService userProfileService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userProfileService = new UserProfileService(userRepository);
    }

    @Test
    void getUserProfile_WhenUserExists_ShouldReturnProfile() {
        String username = "testuser";
        User user = new User(UUID.randomUUID(), "Name", "Surname", "email@test.com", username, "hash", "desc", "pic", "banner");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserProfileResponse response = userProfileService.getUserProfile(username);

        assertEquals(username, response.username());
        assertEquals("Name", response.name());
        assertEquals("email@test.com", response.email());
    }

    @Test
    void getUserProfile_WhenUserNotFound_ShouldThrowException() {
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userProfileService.getUserProfile(username));
    }

    @Test
    void updateUserProfile_WhenUserExists_ShouldUpdateAndReturnProfile() {
        String username = "testuser";
        User existingUser = new User(UUID.randomUUID(), "Name", "Surname", "email@test.com", username, "hash", "oldDesc", "oldPic", "oldBanner");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateUserCommand command = new UpdateUserCommand(username, "newDesc", "newPic", "newBanner");
        UserProfileResponse response = userProfileService.updateUserProfile(command);

        assertEquals("newDesc", response.description());
        assertEquals("newPic", response.profilePictureUrl());
        assertEquals("newBanner", response.bannerUrl());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserProfile_WhenUserNotFound_ShouldThrowException() {
        UpdateUserCommand command = new UpdateUserCommand("nonexistent", "desc", "pic", "banner");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userProfileService.updateUserProfile(command));
        verify(userRepository, never()).save(any());
    }
}
