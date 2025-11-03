package com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper;

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
        UserEntity entity = new UserEntity(1L, "John", "Doe", "john@example.com", "johndoe", "hashedPassword");

        User domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.id());
        assertEquals("John", domain.name());
        assertEquals("Doe", domain.surname());
        assertEquals("john@example.com", domain.email());
        assertEquals("johndoe", domain.username());
        assertEquals("hashedPassword", domain.hashedPassword());
    }

    @Test
    void testToEntity() {
        User domain = new User(2L, "Jane", "Smith", "jane@example.com", "janesmith", "hashedPassword");

        UserEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Jane", entity.getName());
        assertEquals("Smith", entity.getSurname());
        assertEquals("jane@example.com", entity.getEmail());
        assertEquals("janesmith", entity.getUsername());
        assertEquals("hashedPassword", entity.getPassword());
    }

    @Test
    void testToDomainThenToEntity() {
        UserEntity originalEntity = new UserEntity(3L, "Bob", "Johnson", "bob@example.com", "bobjohnson", "pwd");

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
        User originalDomain = new User(4L, "Alice", "Brown", "alice@example.com", "alicebrown", "pwd");

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
        UserEntity entity = new UserEntity(null, "Test", "User", "test@example.com", "testuser", "pwd");

        User domain = mapper.toDomain(entity);

        assertNull(domain.id());
        assertEquals("Test", domain.name());
        assertEquals("testuser", domain.username());
    }

    @Test
    void testToEntityWithoutId() {
        User domain = new User(null, "NoId", "User", "noid@example.com", "noiduser", "pwd");

        UserEntity entity = mapper.toEntity(domain);

        assertNull(entity.getId());
        assertEquals("NoId", entity.getName());
        assertEquals("noiduser", entity.getUsername());
    }

    @Test
    void testToDomainMultipleUsers() {
        UserEntity entity1 = new UserEntity(1L, "User1", "Last1", "user1@example.com", "user1", "pwd1");
        UserEntity entity2 = new UserEntity(2L, "User2", "Last2", "user2@example.com", "user2", "pwd2");

        User domain1 = mapper.toDomain(entity1);
        User domain2 = mapper.toDomain(entity2);

        assertEquals("user1", domain1.username());
        assertEquals("user2", domain2.username());
        assertNotEquals(domain1.id(), domain2.id());
    }

    @Test
    void testToEntityMultipleUsers() {
        User domain1 = new User(1L, "User1", "Last1", "user1@example.com", "user1", "pwd1");
        User domain2 = new User(2L, "User2", "Last2", "user2@example.com", "user2", "pwd2");

        UserEntity entity1 = mapper.toEntity(domain1);
        UserEntity entity2 = mapper.toEntity(domain2);

        assertEquals("user1", entity1.getUsername());
        assertEquals("user2", entity2.getUsername());
        assertNotEquals(entity1.getId(), entity2.getId());
    }

    @Test
    void testToDomainWithSpecialCharacters() {
        UserEntity entity = new UserEntity(1L, "José", "García", "josé@example.com", "josé_garcía", "pwd");

        User domain = mapper.toDomain(entity);

        assertEquals("José", domain.name());
        assertEquals("josé_garcía", domain.username());
    }
}
