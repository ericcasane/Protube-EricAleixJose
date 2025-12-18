package com.tecnocampus.LS2.protube_back.application.service;

import com.tecnocampus.LS2.protube_back.adapter.out.in_memory.UserRepository;
import com.tecnocampus.LS2.protube_back.application.port.in.command.UpdateUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.response.UserProfileResponse;
import com.tecnocampus.LS2.protube_back.application.port.in.usecase.GetUserProfileUseCase;
import com.tecnocampus.LS2.protube_back.application.port.in.usecase.UpdateUserProfileUseCase;
import com.tecnocampus.LS2.protube_back.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService implements GetUserProfileUseCase, UpdateUserProfileUseCase {

    private final UserRepository userRepository;

    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserProfileResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    @Override
    public UserProfileResponse updateUserProfile(UpdateUserCommand command) {
        User user = userRepository.findByUsername(command.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        User updatedUser = new User(
                user.id(),
                user.name(),
                user.surname(),
                user.email(),
                user.username(),
                user.hashedPassword(),
                command.description(),
                command.profilePictureUrl(),
                command.bannerUrl()
        );

        User savedUser = userRepository.save(updatedUser);
        return mapToResponse(savedUser);
    }

    private UserProfileResponse mapToResponse(User user) {
        return new UserProfileResponse(
                user.username(),
                user.name(),
                user.surname(),
                user.email(),
                user.description(),
                user.profilePictureUrl(),
                user.bannerUrl()
        );
    }
}

