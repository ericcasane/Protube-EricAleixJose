package com.tecnocampus.LS2.protube_back.application.service;

import com.tecnocampus.LS2.protube_back.application.port.in.command.AuthenticateUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.usecase.AuthenticateUserUseCase;
import com.tecnocampus.LS2.protube_back.application.port.in.response.UserAuthResponse;
import com.tecnocampus.LS2.protube_back.adapter.out.in_memory.UserRepository;
import com.tecnocampus.LS2.protube_back.domain.model.User;
import com.tecnocampus.LS2.protube_back.adapter.out.security.component.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserService implements AuthenticateUserUseCase {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticateUserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserAuthResponse authenticate(AuthenticateUserCommand command) {
        User user = userRepository.findByUsername(command.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!user.validatePassword(command.password())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtTokenProvider.generateToken(user);

        return new UserAuthResponse(token, user.id(), user.username(), user.email());
    }
}
