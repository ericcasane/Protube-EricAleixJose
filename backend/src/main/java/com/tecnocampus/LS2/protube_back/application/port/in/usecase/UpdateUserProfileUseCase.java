package com.tecnocampus.LS2.protube_back.application.port.in.usecase;

import com.tecnocampus.LS2.protube_back.application.port.in.command.UpdateUserCommand;
import com.tecnocampus.LS2.protube_back.application.port.in.response.UserProfileResponse;

public interface UpdateUserProfileUseCase {
    UserProfileResponse updateUserProfile(UpdateUserCommand command);
}

