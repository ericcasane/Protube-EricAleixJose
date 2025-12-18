package com.tecnocampus.LS2.protube_back.adapter.in.web;

import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.UpdateUserRequest;
import com.tecnocampus.LS2.protube_back.application.port.in.command.UpdateUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.response.UserProfileResponse;
import com.tecnocampus.LS2.protube_back.application.port.in.usecase.GetUserProfileUseCase;
import com.tecnocampus.LS2.protube_back.application.port.in.usecase.UpdateUserProfileUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;

    public UserController(GetUserProfileUseCase getUserProfileUseCase, UpdateUserProfileUseCase updateUserProfileUseCase) {
        this.getUserProfileUseCase = getUserProfileUseCase;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String username) {
        return ResponseEntity.ok(getUserProfileUseCase.getUserProfile(username));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateUserRequest request) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        UpdateUserCommand command = new UpdateUserCommand(
                userDetails.getUsername(),
                request.description(),
                request.profilePictureUrl(),
                request.bannerUrl()
        );

        return ResponseEntity.ok(updateUserProfileUseCase.updateUserProfile(command));
    }
}

