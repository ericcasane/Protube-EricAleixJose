package com.tecnocampus.LS2.protube_back.domain.model;

import com.tecnocampus.LS2.protube_back.application.port.in.command.RegisterUserCommand;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public record User(
        UUID id,
        String name,
        String surname,
        String email,
        String username,
        String hashedPassword
) {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User(String name, String surname, String email, String username, String hashedPassword) {
        this(null, name, surname, email, username, hashedPassword);
    }

    public static User from(RegisterUserCommand command) {
        return new User(
                command.name(),
                command.surname(),
                command.email(),
                command.username(),
                encoder.encode(command.password())
        );
    }

    public static User from(UUID id, String name, String surname, String email, String username, String hashedPassword) {
        return new User(id, name, surname, email, username, hashedPassword);
    }

    public boolean validatePassword(String rawPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
