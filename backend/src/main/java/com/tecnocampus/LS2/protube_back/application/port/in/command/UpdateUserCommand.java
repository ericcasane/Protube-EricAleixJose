package com.tecnocampus.LS2.protube_back.application.port.in.command;

public record UpdateUserCommand(
        String username, // identify user
        String description,
        String profilePictureUrl,
        String bannerUrl
) {}

