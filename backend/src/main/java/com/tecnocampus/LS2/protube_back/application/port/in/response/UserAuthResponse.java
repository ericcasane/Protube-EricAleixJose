package com.tecnocampus.LS2.protube_back.application.port.in.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserAuthResponse {
    private final String token;
    private final UUID userId;
    private final String username;
    private final String email;
}
