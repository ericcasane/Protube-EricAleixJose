package com.tecnocampus.LS2.protube_back.application.port.in;

public interface RegisterUserUseCase {
    UserAuthResponse registerUser(RegisterUserCommand command);
}
