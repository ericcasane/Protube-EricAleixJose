package com.tecnocampus.LS2.protube_back.domain.model;

import com.tecnocampus.LS2.protube_back.application.port.in.command.RegisterUserCommand;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void testUserConstructorWithId() {
        String hashedPassword = encoder.encode("password123");
        User user = new User(1L, "John", "Doe", "john@example.com", "johndoe", hashedPassword);

        assertEquals(1L, user.id());
        assertEquals("John", user.name());
        assertEquals("Doe", user.surname());
        assertEquals("john@example.com", user.email());
        assertEquals("johndoe", user.username());
        assertEquals(hashedPassword, user.hashedPassword());
    }

    @Test
    void testUserConstructorWithoutId() {
        String hashedPassword = encoder.encode("password123");
        User user = new User("Jane", "Smith", "jane@example.com", "janesmith", hashedPassword);

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
                "myPassword123"
        );

        User user = User.from(command);

        assertNull(user.id());
        assertEquals("Bob", user.name());
        assertEquals("Johnson", user.surname());
        assertEquals("bob@example.com", user.email());
        assertEquals("bobjohnson", user.username());
        assertNotNull(user.hashedPassword());
        assertNotEquals("myPassword123", user.hashedPassword()); // Password should be encoded
    }

    @Test
    void testUserFromStaticFactory() {
        String hashedPassword = encoder.encode("password123");
        User user = User.from(2L, "Alice", "Brown", "alice@example.com", "alicebrown", hashedPassword);

        assertEquals(2L, user.id());
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
        User user = new User(1L, "Test", "User", "test@example.com", "testuser", hashedPassword);

        assertTrue(user.validatePassword(rawPassword));
    }

    @Test
    void testValidatePasswordFailure() {
        String rawPassword = "correctPassword123";
        String hashedPassword = encoder.encode(rawPassword);
        User user = new User(1L, "Test", "User", "test@example.com", "testuser", hashedPassword);

        assertFalse(user.validatePassword("wrongPassword123"));
    }

    @Test
    void testValidatePasswordWithEmptyString() {
        String hashedPassword = encoder.encode("password123");
        User user = new User(1L, "Test", "User", "test@example.com", "testuser", hashedPassword);

        assertFalse(user.validatePassword(""));
    }

    @Test
    void testUserEquality() {
        String hashedPassword = encoder.encode("password123");
        User user1 = new User(1L, "Test", "User", "test@example.com", "testuser", hashedPassword);
        User user2 = new User(1L, "Test", "User", "test@example.com", "testuser", hashedPassword);

        assertEquals(user1, user2);
    }

    @Test
    void testUserRecord() {
        String hashedPassword = encoder.encode("password123");
        User user1 = new User(1L, "Test", "User", "test@example.com", "testuser", hashedPassword);
        User user2 = user1;

        assertSame(user1, user2);
    }
}
