package com.tecnocampus.LS2.protube_back.application.port.in.usecase;

import com.tecnocampus.LS2.protube_back.application.port.in.command.RegisterUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.response.UserAuthResponse;

public interface RegisterUserUseCase {
    UserAuthResponse registerUser(RegisterUserCommand command);
}
