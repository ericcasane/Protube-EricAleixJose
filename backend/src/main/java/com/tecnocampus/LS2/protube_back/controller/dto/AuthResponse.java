package com.tecnocampus.LS2.protube_back.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthResponse {
    private String token;
    private Long userId;
    private String username;
    private String email;
}
