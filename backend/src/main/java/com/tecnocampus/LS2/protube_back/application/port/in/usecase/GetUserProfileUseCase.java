package com.tecnocampus.LS2.protube_back.application.port.in.usecase;

import com.tecnocampus.LS2.protube_back.application.port.in.response.UserProfileResponse;

public interface GetUserProfileUseCase {
    UserProfileResponse getUserProfile(String username);
}

