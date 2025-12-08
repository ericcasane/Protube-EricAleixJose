package com.tecnocampus.LS2.protube_back.application.port.in.response;

public record UserProfileResponse(
        String username,
        String name,
        String surname,
        String email,
        String description,
        String profilePictureUrl,
        String bannerUrl
) {}

