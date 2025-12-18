package com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper;


import java.util.UUID;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.UserEntity;
import com.tecnocampus.LS2.protube_back.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {

    private UserEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserEntityMapper();
    }

    @Test
    void testToDomain() {
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity(id, "John", "Doe", "john@example.com", "johndoe", "hashedPassword", null, null, null);

        User domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.id());
        assertEquals("John", domain.name());
        assertEquals("Doe", domain.surname());
        assertEquals("john@example.com", domain.email());
        assertEquals("johndoe", domain.username());
        assertEquals("hashedPassword", domain.hashedPassword());
    }

    @Test
    void testToEntity() {
        UUID id = UUID.randomUUID();
        User domain = User.from(id, "Jane", "Smith", "jane@example.com", "janesmith", "hashedPassword", null, null, null);

        UserEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Jane", entity.getName());
        assertEquals("Smith", entity.getSurname());
        assertEquals("jane@example.com", entity.getEmail());
        assertEquals("janesmith", entity.getUsername());
        assertEquals("hashedPassword", entity.getPassword());
    }

    @Test
    void testToDomainThenToEntity() {
        UUID id = UUID.randomUUID();
        UserEntity originalEntity = new UserEntity(id, "Bob", "Johnson", "bob@example.com", "bobjohnson", "pwd", null, null, null);

        User domain = mapper.toDomain(originalEntity);
        UserEntity mappedEntity = mapper.toEntity(domain);

        assertEquals(originalEntity.getId(), mappedEntity.getId());
        assertEquals(originalEntity.getName(), mappedEntity.getName());
        assertEquals(originalEntity.getSurname(), mappedEntity.getSurname());
        assertEquals(originalEntity.getEmail(), mappedEntity.getEmail());
        assertEquals(originalEntity.getUsername(), mappedEntity.getUsername());
        assertEquals(originalEntity.getPassword(), mappedEntity.getPassword());
    }

    @Test
    void testToEntityThenToDomain() {
        UUID id = UUID.randomUUID();
        User originalDomain = User.from(id, "Alice", "Brown", "alice@example.com", "alicebrown", "pwd", null, null, null);

        UserEntity entity = mapper.toEntity(originalDomain);
        User mappedDomain = mapper.toDomain(entity);

        assertEquals(originalDomain.id(), mappedDomain.id());
        assertEquals(originalDomain.name(), mappedDomain.name());
        assertEquals(originalDomain.surname(), mappedDomain.surname());
        assertEquals(originalDomain.email(), mappedDomain.email());
        assertEquals(originalDomain.username(), mappedDomain.username());
        assertEquals(originalDomain.hashedPassword(), mappedDomain.hashedPassword());
    }

    @Test
    void testToDomainWithoutId() {
        UserEntity entity = new UserEntity(null, "Test", "User", "test@example.com", "testuser", "pwd", null, null, null);

        User domain = mapper.toDomain(entity);

        assertNull(domain.id());
        assertEquals("Test", domain.name());
        assertEquals("testuser", domain.username());
    }

    @Test
    void testToEntityWithoutId() {
        User domain = new User("NoId", "User", "noid@example.com", "noiduser", "pwd");

        UserEntity entity = mapper.toEntity(domain);

        assertNull(entity.getId());
        assertEquals("NoId", entity.getName());
        assertEquals("noiduser", entity.getUsername());
    }

    @Test
    void testToDomainMultipleUsers() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UserEntity entity1 = new UserEntity(id1, "User1", "Last1", "user1@example.com", "user1", "pwd1", null, null, null);
        UserEntity entity2 = new UserEntity(id2, "User2", "Last2", "user2@example.com", "user2", "pwd2", null, null, null);

        User domain1 = mapper.toDomain(entity1);
        User domain2 = mapper.toDomain(entity2);

        assertEquals("user1", domain1.username());
        assertEquals("user2", domain2.username());
        assertNotEquals(domain1.id(), domain2.id());
    }

    @Test
    void testToEntityMultipleUsers() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User domain1 = User.from(id1, "User1", "Last1", "user1@example.com", "user1", "pwd1", null, null, null);
        User domain2 = User.from(id2, "User2", "Last2", "user2@example.com", "user2", "pwd2", null, null, null);

        UserEntity entity1 = mapper.toEntity(domain1);
        UserEntity entity2 = mapper.toEntity(domain2);

        assertEquals("user1", entity1.getUsername());
        assertEquals("user2", entity2.getUsername());
        assertNotEquals(entity1.getId(), entity2.getId());
    }

    @Test
    void testToDomainWithSpecialCharacters() {
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity(id, "José", "García", "josé@example.com", "josé_garcía", "pwd", null, null, null);

        User domain = mapper.toDomain(entity);

        assertEquals("José", domain.name());
        assertEquals("josé_garcía", domain.username());
    }
}
