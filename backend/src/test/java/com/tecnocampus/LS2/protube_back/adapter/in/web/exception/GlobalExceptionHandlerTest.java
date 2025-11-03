package com.tecnocampus.LS2.protube_back.adapter.in.web.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleIllegalArgumentException() {
        String errorMessage = "Email already registered";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
    }

    @Test
    void testHandleIllegalArgumentExceptionWithDifferentMessage() {
        String errorMessage = "Invalid username or password";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().getMessage());
    }

    @Test
    void testErrorResponseCreation() {
        String message = "Test error message";
        ErrorResponse errorResponse = new ErrorResponse(message);

        assertEquals(message, errorResponse.getMessage());
    }

    @Test
    void testHandleIllegalArgumentExceptionWithNullMessage() {
        IllegalArgumentException exception = new IllegalArgumentException((String) null);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getMessage());
    }

    @Test
    void testHandleValidationExceptionsWithSingleFieldError() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("user", "email", "Email is required");
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldError);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals("Email is required", body.get("email"));
    }

    @Test
    void testHandleValidationExceptionsWithMultipleFieldErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError emailError = new FieldError("user", "email", "Email is required");
        FieldError nameError = new FieldError("user", "name", "Name must not be blank");
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(emailError);
        fieldErrors.add(nameError);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
        assertEquals("Email is required", body.get("email"));
        assertEquals("Name must not be blank", body.get("name"));
    }

    @Test
    void testHandleValidationExceptionsWithNoFieldErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(new ArrayList<>());

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isEmpty());
    }

    @Test
    void testHandleValidationExceptionsWithDifferentFieldNames() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError usernameError = new FieldError("user", "username", "Username must be unique");
        FieldError passwordError = new FieldError("user", "password", "Password must be at least 8 characters");
        FieldError ageError = new FieldError("user", "age", "Age must be a positive number");

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(usernameError);
        fieldErrors.add(passwordError);
        fieldErrors.add(ageError);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertEquals(3, body.size());
        assertEquals("Username must be unique", body.get("username"));
        assertEquals("Password must be at least 8 characters", body.get("password"));
        assertEquals("Age must be a positive number", body.get("age"));
    }
}
