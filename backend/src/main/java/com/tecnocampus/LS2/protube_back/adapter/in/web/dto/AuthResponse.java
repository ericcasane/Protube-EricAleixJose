package com.tecnocampus.LS2.protube_back.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class AuthResponse {
    private String token;
    private UUID userId;
    private String username;
    private String email;
}
