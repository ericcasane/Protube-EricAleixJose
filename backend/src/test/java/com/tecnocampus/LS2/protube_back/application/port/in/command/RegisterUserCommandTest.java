package com.tecnocampus.LS2.protube_back.application.port.in.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserCommandTest {

    @Test
    void testRegisterUserCommandCreation() {
        RegisterUserCommand command = new RegisterUserCommand(
                "John",
                "Doe",
                "john@example.com",
                "johndoe",
                "password123"
        );

        assertEquals("John", command.name());
        assertEquals("Doe", command.surname());
        assertEquals("john@example.com", command.email());
        assertEquals("johndoe", command.username());
        assertEquals("password123", command.password());
    }

    @Test
    void testRegisterUserCommandEquality() {
        RegisterUserCommand command1 = new RegisterUserCommand(
                "John",
                "Doe",
                "john@example.com",
                "johndoe",
                "password123"
        );

        RegisterUserCommand command2 = new RegisterUserCommand(
                "John",
                "Doe",
                "john@example.com",
                "johndoe",
                "password123"
        );

        assertEquals(command1, command2);
    }

    @Test
    void testRegisterUserCommandInequality() {
        RegisterUserCommand command1 = new RegisterUserCommand(
                "John",
                "Doe",
                "john@example.com",
                "johndoe",
                "password123"
        );

        RegisterUserCommand command2 = new RegisterUserCommand(
                "Jane",
                "Smith",
                "jane@example.com",
                "janesmith",
                "password456"
        );

        assertNotEquals(command1, command2);
    }

    @Test
    void testRegisterUserCommandToString() {
        RegisterUserCommand command = new RegisterUserCommand(
                "John",
                "Doe",
                "john@example.com",
                "johndoe",
                "password123"
        );

        String str = command.toString();
        assertNotNull(str);
        assertTrue(str.contains("John"));
        assertTrue(str.contains("john@example.com"));
    }
}
