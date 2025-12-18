package com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.CommentEntity;
import com.tecnocampus.LS2.protube_back.domain.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CommentEntityMapperTest {

    private CommentEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CommentEntityMapper();
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        Comment comment = new Comment();
        String id = UUID.randomUUID().toString();
        comment.setId(id);
        comment.setAuthor("author");
        comment.setText("text");
        comment.setTimestamp(123456789L);
        comment.setLikeCount(5L);

        CommentEntity entity = mapper.toEntity(comment);

        assertEquals(id, entity.getId().toString());
        assertEquals("author", entity.getAuthor());
        assertEquals("text", entity.getText());
        assertEquals(123456789L, entity.getTimestamp());
        assertEquals(5L, entity.getLikeCount());
    }

    @Test
    void toEntity_NullId_ShouldMapToNull() {
        Comment comment = new Comment();
        comment.setId(null);

        CommentEntity entity = mapper.toEntity(comment);

        assertNull(entity.getId());
    }

    @Test
    void toDomain_ShouldMapCorrectly() {
        CommentEntity entity = new CommentEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setAuthor("author");
        entity.setText("text");
        entity.setTimestamp(123456789L);
        entity.setLikeCount(5L);

        Comment comment = mapper.toDomain(entity);

        assertEquals(id.toString(), comment.getId());
        assertEquals("author", comment.getAuthor());
        assertEquals("text", comment.getText());
        assertEquals(123456789L, comment.getTimestamp());
        assertEquals(5L, comment.getLikeCount());
    }

    @Test
    void toDomain_NullId_ShouldMapToNull() {
        CommentEntity entity = new CommentEntity();
        entity.setId(null);

        Comment comment = mapper.toDomain(entity);

        assertNull(comment.getId());
    }
}
