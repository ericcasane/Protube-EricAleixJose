package com.tecnocampus.LS2.protube_back.application.port.in;

public interface AuthenticateUserUseCase {
    UserAuthResponse authenticate(AuthenticateUserCommand command);
}
