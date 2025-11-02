package com.tecnocampus.LS2.protube_back.infrastructure.adapter.out.persistence.jpa;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.VideoEntity;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class VideoEntityTest {

    @Test
    void testConstructorWithoutId() {
        VideoEntity entity = new VideoEntity(
                "Test Video",
                "testuser",
                "video.mp4",
                "thumbnail.webp"
        );

        assertNull(entity.getId());
        assertEquals("Test Video", entity.getTitle());
        assertEquals("testuser", entity.getUser());
        assertEquals("video.mp4", entity.getVideoFileName());
        assertEquals("thumbnail.webp", entity.getThumbnailFileName());
    }

    @Test
    void testConstructorWithAllParameters() {
        UUID id = UUID.randomUUID();

        VideoEntity entity = new VideoEntity(
                id,
                "Test Video",
                "testuser",
                "video.mp4",
                "thumbnail.webp"
        );

        assertEquals(id, entity.getId());
        assertEquals("Test Video", entity.getTitle());
        assertEquals("testuser", entity.getUser());
        assertEquals("video.mp4", entity.getVideoFileName());
        assertEquals("thumbnail.webp", entity.getThumbnailFileName());
    }

    @Test
    void testNoArgsConstructor() {
        VideoEntity entity = new VideoEntity();

        assertNull(entity.getId());
        assertNull(entity.getTitle());
        assertNull(entity.getUser());
        assertNull(entity.getVideoFileName());
        assertNull(entity.getThumbnailFileName());
    }

    @Test
    void testSetters() {
        VideoEntity entity = new VideoEntity();
        UUID id = UUID.randomUUID();

        entity.setId(id);
        entity.setTitle("Updated Video");
        entity.setUser("updateduser");
        entity.setVideoFileName("updated.mp4");
        entity.setThumbnailFileName("updated.webp");

        assertEquals(id, entity.getId());
        assertEquals("Updated Video", entity.getTitle());
        assertEquals("updateduser", entity.getUser());
        assertEquals("updated.mp4", entity.getVideoFileName());
        assertEquals("updated.webp", entity.getThumbnailFileName());
    }
}
