package com.tecnocampus.LS2.protube_back.adapter.in.web.dto;

public record UpdateUserRequest(
        String description,
        String profilePictureUrl,
        String bannerUrl
) {}

