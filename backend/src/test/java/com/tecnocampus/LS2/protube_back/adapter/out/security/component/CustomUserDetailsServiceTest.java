package com.tecnocampus.LS2.protube_back.adapter.out.security.component;

import com.tecnocampus.LS2.protube_back.adapter.out.in_memory.UserRepository;
import com.tecnocampus.LS2.protube_back.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        String username = "johndoe";
        String hashedPassword = "hashed_password";
        User user = new User(1L, "John", "Doe", "john@example.com", username, hashedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(hashedPassword, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());

        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        String username = "nonexistent";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername(username)
        );

        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoadUserByUsernameWithSpecialCharacters() {
        String username = "user_special.123";
        String hashedPassword = "hashed_pwd";
        User user = new User(2L, "User", "Special", "special@example.com", username, hashedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());

        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoadUserByUsernameMultipleUsers() {
        String username1 = "alice";
        String username2 = "bob";

        User user1 = new User(1L, "Alice", "Brown", "alice@example.com", username1, "hash1");
        User user2 = new User(2L, "Bob", "Johnson", "bob@example.com", username2, "hash2");

        when(userRepository.findByUsername(username1)).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername(username2)).thenReturn(Optional.of(user2));

        UserDetails details1 = customUserDetailsService.loadUserByUsername(username1);
        UserDetails details2 = customUserDetailsService.loadUserByUsername(username2);

        assertEquals(username1, details1.getUsername());
        assertEquals(username2, details2.getUsername());
        assertNotEquals(details1.getPassword(), details2.getPassword());
    }

    @Test
    void testLoadUserByUsernameErrorMessage() {
        String username = "missing_user";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(username)
        );

        assertTrue(exception.getMessage().contains(username));
    }

    @Test
    void testLoadUserByUsernameAuthoritiesAreEmpty() {
        String username = "testuser";
        User user = new User(1L, "Test", "User", "test@example.com", username, "hashed");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertTrue(userDetails.getAuthorities().isEmpty());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
}
