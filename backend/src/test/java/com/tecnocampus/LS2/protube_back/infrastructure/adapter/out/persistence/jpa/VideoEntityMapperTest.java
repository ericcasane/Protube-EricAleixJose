package com.tecnocampus.LS2.protube_back.infrastructure.adapter.out.persistence.jpa;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.VideoEntity;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper.VideoEntityMapper;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class VideoEntityMapperTest {

    private VideoEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new VideoEntityMapper();
    }

    @Test
    void testToEntity() {
        Video video = new Video();
        video.setTitle("Test Video");
        video.setUser("testuser");
        video.setVideoFileName("video.mp4");
        video.setThumbnailFileName("thumbnail.webp");

        VideoEntity entity = mapper.toEntity(video);

        assertNotNull(entity);
        assertEquals(video.getTitle(), entity.getTitle());
        assertEquals(video.getUser(), entity.getUser());
        assertEquals(video.getVideoFileName(), entity.getVideoFileName());
        assertEquals(video.getThumbnailFileName(), entity.getThumbnailFileName());
    }

    @Test
    void testToDomain() {
        VideoEntity entity = new VideoEntity();
        entity.setId(UUID.randomUUID());
        entity.setTitle("Test Video");
        entity.setUser("testuser");
        entity.setVideoFileName("video.mp4");
        entity.setThumbnailFileName("thumbnail.webp");

        Video video = mapper.toDomain(entity);

        assertNotNull(video);
        assertEquals(entity.getTitle(), video.getTitle());
        assertEquals(entity.getUser(), video.getUser());
        assertEquals(entity.getVideoFileName(), video.getVideoFileName());
        assertEquals(entity.getThumbnailFileName(), video.getThumbnailFileName());
    }

    @Test
    void testRoundTripMapping() {
        Video originalVideo = new Video();
        originalVideo.setTitle("Original Video");
        originalVideo.setUser("originaluser");
        originalVideo.setVideoFileName("original.mp4");
        originalVideo.setThumbnailFileName("original.webp");

        VideoEntity entity = mapper.toEntity(originalVideo);
        Video mappedVideo = mapper.toDomain(entity);

        assertEquals(originalVideo.getTitle(), mappedVideo.getTitle());
        assertEquals(originalVideo.getUser(), mappedVideo.getUser());
        assertEquals(originalVideo.getVideoFileName(), mappedVideo.getVideoFileName());
        assertEquals(originalVideo.getThumbnailFileName(), mappedVideo.getThumbnailFileName());
    }
}
