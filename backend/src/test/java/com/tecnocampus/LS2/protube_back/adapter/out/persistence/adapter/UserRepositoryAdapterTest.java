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


import java.util.UUID;

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
        UUID savedId = UUID.randomUUID();
        User user = User.from(null, "John", "Doe", "john@example.com", "johndoe", "hashedPwd", null, null, null);
        UserEntity entity = new UserEntity(null, "John", "Doe", "john@example.com", "johndoe", "hashedPwd", null, null, null);
        UserEntity savedEntity = new UserEntity(savedId, "John", "Doe", "john@example.com", "johndoe", "hashedPwd", null, null, null);
        User savedUser = User.from(savedId, "John", "Doe", "john@example.com", "johndoe", "hashedPwd", null, null, null);

        when(mapper.toEntity(user)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedUser);

        User result = adapter.save(user);

        assertNotNull(result);
        assertEquals(savedId, result.id());
        assertEquals("John", result.name());
        assertEquals("johndoe", result.username());

        verify(mapper).toEntity(user);
        verify(jpaRepository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void testFindByEmailFound() {
        UUID id = UUID.randomUUID();
        String email = "john@example.com";
        UserEntity entity = new UserEntity(id, "John", "Doe", email, "johndoe", "hashedPwd", null, null, null);
        User user = User.from(id, "John", "Doe", email, "johndoe", "hashedPwd", null, null, null);

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
        UUID id = UUID.randomUUID();
        String username = "johndoe";
        UserEntity entity = new UserEntity(id, "John", "Doe", "john@example.com", username, "hashedPwd", null, null, null);
        User user = User.from(id, "John", "Doe", "john@example.com", username, "hashedPwd", null, null, null);

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
        UUID savedId = UUID.randomUUID();
        User newUser = User.from(null, "Jane", "Smith", "jane@example.com", "janesmith", "pwd", null, null, null);
        UserEntity entityToSave = new UserEntity(null, "Jane", "Smith", "jane@example.com", "janesmith", "pwd", null, null, null);
        UserEntity savedEntity = new UserEntity(savedId, "Jane", "Smith", "jane@example.com", "janesmith", "pwd", null, null, null);
        User savedUser = User.from(savedId, "Jane", "Smith", "jane@example.com", "janesmith", "pwd", null, null, null);

        when(mapper.toEntity(newUser)).thenReturn(entityToSave);
        when(jpaRepository.save(entityToSave)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedUser);
        when(jpaRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(savedEntity));

        User result = adapter.save(newUser);
        assertEquals(savedId, result.id());

        when(mapper.toDomain(savedEntity)).thenReturn(savedUser);
        Optional<User> foundUser = adapter.findByEmail("jane@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals(savedId, foundUser.get().id());
    }

    @Test
    void testFindByEmailAndUsername() {
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity(id, "Bob", "Johnson", "bob@example.com", "bobjohnson", "pwd", null, null, null);
        User user = User.from(id, "Bob", "Johnson", "bob@example.com", "bobjohnson", "pwd", null, null, null);

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
