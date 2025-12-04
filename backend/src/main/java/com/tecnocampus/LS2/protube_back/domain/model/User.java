package com.tecnocampus.LS2.protube_back.domain.model;

import com.tecnocampus.LS2.protube_back.application.port.in.command.RegisterUserCommand;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record User(
        Long id,
        String name,
        String surname,
        String email,
        String username,
        String hashedPassword,
        String description,
        String profilePictureUrl,
        String bannerUrl
) {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Constructor for registration (defaults for profile fields)
    public User(String name, String surname, String email, String username, String hashedPassword) {
        this(null, name, surname, email, username, hashedPassword, "", null, null);
    }

    public static User from(RegisterUserCommand command) {
        return new User(
                null,
                command.name(),
                command.surname(),
                command.email(),
                command.username(),
                encoder.encode(command.password()),
                "Hello! I am using ProTube.", // Default description
                "https://ui-avatars.com/api/?name=" + command.name() + "+" + command.surname(), // Default avatar
                "https://picsum.photos/seed/" + command.username() + "/1200/300" // Default banner
        );
    }

    public static User from(Long id, String name, String surname, String email, String username, String hashedPassword, String description, String profilePictureUrl, String bannerUrl) {
        return new User(id, name, surname, email, username, hashedPassword, description, profilePictureUrl, bannerUrl);
    }

    public boolean validatePassword(String rawPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
