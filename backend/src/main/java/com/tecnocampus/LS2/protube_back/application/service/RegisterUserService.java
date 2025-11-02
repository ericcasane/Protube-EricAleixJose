package com.tecnocampus.LS2.protube_back.application.service;

import com.tecnocampus.LS2.protube_back.application.port.in.RegisterUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.RegisterUserUseCase;
import com.tecnocampus.LS2.protube_back.application.port.in.UserAuthResponse;
import com.tecnocampus.LS2.protube_back.application.port.out.UserRepository;
import com.tecnocampus.LS2.protube_back.domain.model.User;
import com.tecnocampus.LS2.protube_back.infrastructure.adapter.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService implements RegisterUserUseCase {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public RegisterUserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserAuthResponse registerUser(RegisterUserCommand command) {
        if (userRepository.findByEmail(command.email()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (userRepository.findByUsername(command.username()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }

        User user = User.from(command);
        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser);

        return new UserAuthResponse(token, savedUser.id(), savedUser.username(), savedUser.email());
    }
}
