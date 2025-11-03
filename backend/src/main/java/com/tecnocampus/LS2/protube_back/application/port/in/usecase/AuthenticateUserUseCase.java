package com.tecnocampus.LS2.protube_back.application.port.in.usecase;

import com.tecnocampus.LS2.protube_back.application.port.in.command.AuthenticateUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.response.UserAuthResponse;

public interface AuthenticateUserUseCase {
    UserAuthResponse authenticate(AuthenticateUserCommand command);
}
