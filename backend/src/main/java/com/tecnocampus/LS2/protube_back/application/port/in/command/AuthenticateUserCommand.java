package com.tecnocampus.LS2.protube_back.application.port.in.command;

import jakarta.validation.constraints.NotBlank;

public record AuthenticateUserCommand(
        @NotBlank(message = "El name de usuario es obligatorio")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}
