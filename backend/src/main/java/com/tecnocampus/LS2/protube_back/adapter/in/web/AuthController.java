package com.tecnocampus.LS2.protube_back.adapter.in.web;

import com.tecnocampus.LS2.protube_back.application.port.in.command.AuthenticateUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.usecase.AuthenticateUserUseCase;
import com.tecnocampus.LS2.protube_back.application.port.in.command.RegisterUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.usecase.RegisterUserUseCase;
import com.tecnocampus.LS2.protube_back.application.port.in.response.UserAuthResponse;
import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.AuthResponse;
import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterUserCommand command) {
        UserAuthResponse response = registerUserUseCase.registerUser(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapToAuthResponse(response));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthenticateUserCommand command) {
        UserAuthResponse response = authenticateUserUseCase.authenticate(command);
        return ResponseEntity.ok(new TokenResponse(response.getToken()));
    }

    private AuthResponse mapToAuthResponse(UserAuthResponse response) {
        return new AuthResponse(
                response.getToken(),
                response.getUserId(),
                response.getUsername(),
                response.getEmail()
        );
    }
}
