package com.tecnocampus.LS2.protube_back.domain.model;

import java.util.UUID;

import com.tecnocampus.LS2.protube_back.application.port.in.command.RegisterUserCommand;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void testUserConstructorWithId() {
        String hashedPassword = encoder.encode("password123");
        UUID id = UUID.randomUUID();
        User user = new User(id, "John", "Doe", "john@example.com", "johndoe", hashedPassword, null, null, null);

        assertEquals(id, user.id());
        assertEquals("John", user.name());
        assertEquals("Doe", user.surname());
        assertEquals("john@example.com", user.email());
        assertEquals("johndoe", user.username());
        assertEquals(hashedPassword, user.hashedPassword());
    }

    @Test
    void testUserConstructorWithoutId() {
        String hashedPassword = encoder.encode("password123");
        User user = new User(null, "Jane", "Smith", "jane@example.com", "janesmith", hashedPassword, null, null, null);

        assertNull(user.id());
        assertEquals("Jane", user.name());
        assertEquals("Smith", user.surname());
        assertEquals("jane@example.com", user.email());
        assertEquals("janesmith", user.username());
        assertEquals(hashedPassword, user.hashedPassword());
    }

    @Test
    void testUserFromRegisterUserCommand() {
        RegisterUserCommand command = new RegisterUserCommand(
                "Bob",
                "Johnson",
                "bob@example.com",
                "bobjohnson",
                "myPassword123",
                "Description"
        );

        User user = User.from(command);

        assertNull(user.id());
        assertEquals("Bob", user.name());
        assertEquals("Johnson", user.surname());
        assertEquals("bob@example.com", user.email());
        assertEquals("bobjohnson", user.username());
        assertEquals("Description", user.description());
        assertNotNull(user.hashedPassword());
        assertNotEquals("myPassword123", user.hashedPassword()); // Password should be encoded
    }

    @Test
    void testUserFromStaticFactory() {
        String hashedPassword = encoder.encode("password123");
        UUID id = UUID.randomUUID();
        User user = User.from(id, "Alice", "Brown", "alice@example.com", "alicebrown", hashedPassword, null, null, null);

        assertEquals(id, user.id());
        assertEquals("Alice", user.name());
        assertEquals("Brown", user.surname());
        assertEquals("alice@example.com", user.email());
        assertEquals("alicebrown", user.username());
        assertEquals(hashedPassword, user.hashedPassword());
    }

    @Test
    void testValidatePasswordSuccess() {
        String rawPassword = "correctPassword123";
        String hashedPassword = encoder.encode(rawPassword);
        User user = User.from(UUID.randomUUID(), "Test", "User", "test@example.com", "testuser", hashedPassword, null, null, null);

        assertTrue(user.validatePassword(rawPassword));
    }

    @Test
    void testValidatePasswordFailure() {
        String rawPassword = "correctPassword123";
        String hashedPassword = encoder.encode(rawPassword);
        User user = User.from(UUID.randomUUID(), "Test", "User", "test@example.com", "testuser", hashedPassword, null, null, null);

        assertFalse(user.validatePassword("wrongPassword123"));
    }

    @Test
    void testValidatePasswordWithEmptyString() {
        String hashedPassword = encoder.encode("password123");
        User user = User.from(UUID.randomUUID(), "Test", "User", "test@example.com", "testuser", hashedPassword, null, null, null);

        assertFalse(user.validatePassword(""));
    }

    @Test
    void testUserEquality() {
        String hashedPassword = encoder.encode("password123");
        UUID id = UUID.randomUUID();
        User user1 = new User(id, "Test", "User", "test@example.com", "testuser", hashedPassword, null, null, null);
        User user2 = new User(id, "Test", "User", "test@example.com", "testuser", hashedPassword, null, null, null);

        assertEquals(user1, user2);
    }

    @Test
    void testUserRecord() {
        String hashedPassword = encoder.encode("password123");
        User user1 = new User(UUID.randomUUID(), "Test", "User", "test@example.com", "testuser", hashedPassword, null, null, null);
        User user2 = user1;

        assertSame(user1, user2);
    }
}
