package com.tecnocampus.LS2.protube_back.adapter.in.web;

import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.UpdateUserRequest;
import com.tecnocampus.LS2.protube_back.application.port.in.command.UpdateUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.response.UserProfileResponse;
import com.tecnocampus.LS2.protube_back.application.port.in.usecase.GetUserProfileUseCase;
import com.tecnocampus.LS2.protube_back.application.port.in.usecase.UpdateUserProfileUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserController userController;
    private GetUserProfileUseCase getUserProfileUseCase;
    private UpdateUserProfileUseCase updateUserProfileUseCase;

    @BeforeEach
    void setUp() {
        getUserProfileUseCase = mock(GetUserProfileUseCase.class);
        updateUserProfileUseCase = mock(UpdateUserProfileUseCase.class);
        userController = new UserController(getUserProfileUseCase, updateUserProfileUseCase);
    }

    @Test
    void getUserProfile_ShouldReturnProfile() {
        String username = "testuser";
        UserProfileResponse response = new UserProfileResponse(username, "Name", "Surname", "email", "desc", "pic", "banner");
        when(getUserProfileUseCase.getUserProfile(username)).thenReturn(response);

        ResponseEntity<UserProfileResponse> result = userController.getUserProfile(username);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
        verify(getUserProfileUseCase).getUserProfile(username);
    }

    @Test
    void updateMyProfile_WhenAuthenticated_ShouldUpdateProfile() {
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        UpdateUserRequest request = new UpdateUserRequest("new desc", "new pic", "new banner");
        
        UserProfileResponse response = new UserProfileResponse("testuser", "Name", "Surname", "email", "new desc", "new pic", "new banner");
        when(updateUserProfileUseCase.updateUserProfile(any(UpdateUserCommand.class))).thenReturn(response);

        ResponseEntity<UserProfileResponse> result = userController.updateMyProfile(userDetails, request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
        verify(updateUserProfileUseCase).updateUserProfile(any(UpdateUserCommand.class));
    }

    @Test
    void updateMyProfile_WhenNotAuthenticated_ShouldReturnUnauthorized() {
        UpdateUserRequest request = new UpdateUserRequest("desc", "pic", "banner");

        ResponseEntity<UserProfileResponse> result = userController.updateMyProfile(null, request);

        assertEquals(401, result.getStatusCode().value());
        verifyNoInteractions(updateUserProfileUseCase);
    }
}
