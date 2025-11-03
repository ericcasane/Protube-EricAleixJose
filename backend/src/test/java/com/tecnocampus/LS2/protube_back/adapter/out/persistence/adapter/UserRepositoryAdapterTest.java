package com.tecnocampus.LS2.protube_back.adapter.out.persistence.adapter;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.UserEntity;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository.UserJpaRepository;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper.UserEntityMapper;
import com.tecnocampus.LS2.protube_back.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository jpaRepository;

    @Mock
    private UserEntityMapper mapper;

    private UserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new UserRepositoryAdapter(jpaRepository, mapper);
    }

    @Test
    void testSaveUser() {
        User user = new User(null, "John", "Doe", "john@example.com", "johndoe", "hashedPwd");
        UserEntity entity = new UserEntity(null, "John", "Doe", "john@example.com", "johndoe", "hashedPwd");
        UserEntity savedEntity = new UserEntity(1L, "John", "Doe", "john@example.com", "johndoe", "hashedPwd");
        User savedUser = new User(1L, "John", "Doe", "john@example.com", "johndoe", "hashedPwd");

        when(mapper.toEntity(user)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedUser);

        User result = adapter.save(user);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John", result.name());
        assertEquals("johndoe", result.username());

        verify(mapper).toEntity(user);
        verify(jpaRepository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void testFindByEmailFound() {
        String email = "john@example.com";
        UserEntity entity = new UserEntity(1L, "John", "Doe", email, "johndoe", "hashedPwd");
        User user = new User(1L, "John", "Doe", email, "johndoe", "hashedPwd");

        when(jpaRepository.findByEmail(email)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(user);

        Optional<User> result = adapter.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        assertEquals("John", result.get().name());

        verify(jpaRepository).findByEmail(email);
        verify(mapper).toDomain(entity);
    }

    @Test
    void testFindByEmailNotFound() {
        String email = "nonexistent@example.com";

        when(jpaRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = adapter.findByEmail(email);

        assertTrue(result.isEmpty());

        verify(jpaRepository).findByEmail(email);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void testFindByUsernameFound() {
        String username = "johndoe";
        UserEntity entity = new UserEntity(1L, "John", "Doe", "john@example.com", username, "hashedPwd");
        User user = new User(1L, "John", "Doe", "john@example.com", username, "hashedPwd");

        when(jpaRepository.findByUsername(username)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(user);

        Optional<User> result = adapter.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        assertEquals("johndoe", result.get().username());

        verify(jpaRepository).findByUsername(username);
        verify(mapper).toDomain(entity);
    }

    @Test
    void testFindByUsernameNotFound() {
        String username = "nonexistent";

        when(jpaRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = adapter.findByUsername(username);

        assertTrue(result.isEmpty());

        verify(jpaRepository).findByUsername(username);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void testSaveAndFindUser() {
        User newUser = new User(null, "Jane", "Smith", "jane@example.com", "janesmith", "pwd");
        UserEntity entityToSave = new UserEntity(null, "Jane", "Smith", "jane@example.com", "janesmith", "pwd");
        UserEntity savedEntity = new UserEntity(2L, "Jane", "Smith", "jane@example.com", "janesmith", "pwd");
        User savedUser = new User(2L, "Jane", "Smith", "jane@example.com", "janesmith", "pwd");

        when(mapper.toEntity(newUser)).thenReturn(entityToSave);
        when(jpaRepository.save(entityToSave)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedUser);
        when(jpaRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(savedEntity));

        User result = adapter.save(newUser);
        assertEquals(2L, result.id());

        when(mapper.toDomain(savedEntity)).thenReturn(savedUser);
        Optional<User> foundUser = adapter.findByEmail("jane@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals(2L, foundUser.get().id());
    }

    @Test
    void testFindByEmailAndUsername() {
        UserEntity entity = new UserEntity(1L, "Bob", "Johnson", "bob@example.com", "bobjohnson", "pwd");
        User user = new User(1L, "Bob", "Johnson", "bob@example.com", "bobjohnson", "pwd");

        when(jpaRepository.findByEmail("bob@example.com")).thenReturn(Optional.of(entity));
        when(jpaRepository.findByUsername("bobjohnson")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(user);

        Optional<User> foundByEmail = adapter.findByEmail("bob@example.com");
        Optional<User> foundByUsername = adapter.findByUsername("bobjohnson");

        assertTrue(foundByEmail.isPresent());
        assertTrue(foundByUsername.isPresent());
        assertEquals(foundByEmail.get().id(), foundByUsername.get().id());
    }
}
