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
                "password123",
                "Test Description"
        );

        assertEquals("John", command.name());
        assertEquals("Doe", command.surname());
        assertEquals("john@example.com", command.email());
        assertEquals("johndoe", command.username());
        assertEquals("password123", command.password());
        assertEquals("Test Description", command.description());
    }

    @Test
    void testRegisterUserCommandEquality() {
        RegisterUserCommand command1 = new RegisterUserCommand(
                "John",
                "Doe",
                "john@example.com",
                "johndoe",
                "password123",
                "Description"
        );

        RegisterUserCommand command2 = new RegisterUserCommand(
                "John",
                "Doe",
                "john@example.com",
                "johndoe",
                "password123",
                "Description"
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
                "password123",
                "Description 1"
        );

        RegisterUserCommand command2 = new RegisterUserCommand(
                "Jane",
                "Smith",
                "jane@example.com",
                "janesmith",
                "password456",
                "Description 2"
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
                "password123",
                "Description"
        );

        String str = command.toString();
        assertNotNull(str);
        assertTrue(str.contains("John"));
        assertTrue(str.contains("john@example.com"));
    }
}
