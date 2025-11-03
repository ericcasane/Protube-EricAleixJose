package com.tecnocampus.LS2.protube_back.application.port.in.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticateUserCommandTest {

    @Test
    void testAuthenticateUserCommandCreation() {
        AuthenticateUserCommand command = new AuthenticateUserCommand("johndoe", "password123");

        assertEquals("johndoe", command.username());
        assertEquals("password123", command.password());
    }

    @Test
    void testAuthenticateUserCommandEquality() {
        AuthenticateUserCommand command1 = new AuthenticateUserCommand("johndoe", "password123");
        AuthenticateUserCommand command2 = new AuthenticateUserCommand("johndoe", "password123");

        assertEquals(command1, command2);
    }

    @Test
    void testAuthenticateUserCommandInequality() {
        AuthenticateUserCommand command1 = new AuthenticateUserCommand("johndoe", "password123");
        AuthenticateUserCommand command2 = new AuthenticateUserCommand("janedoe", "password456");

        assertNotEquals(command1, command2);
    }

    @Test
    void testAuthenticateUserCommandToString() {
        AuthenticateUserCommand command = new AuthenticateUserCommand("johndoe", "password123");

        String str = command.toString();
        assertNotNull(str);
        assertTrue(str.contains("johndoe"));
    }

    @Test
    void testAuthenticateUserCommandWithDifferentUsernameSamePassword() {
        AuthenticateUserCommand command1 = new AuthenticateUserCommand("johndoe", "password123");
        AuthenticateUserCommand command2 = new AuthenticateUserCommand("janedoe", "password123");

        assertNotEquals(command1, command2);
    }

    @Test
    void testAuthenticateUserCommandWithSameUsernameDifferentPassword() {
        AuthenticateUserCommand command1 = new AuthenticateUserCommand("johndoe", "password123");
        AuthenticateUserCommand command2 = new AuthenticateUserCommand("johndoe", "password456");

        assertNotEquals(command1, command2);
    }
}
