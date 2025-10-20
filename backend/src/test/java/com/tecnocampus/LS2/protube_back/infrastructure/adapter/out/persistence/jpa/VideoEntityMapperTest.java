package com.tecnocampus.LS2.protube_back.infrastructure.adapter.out.persistence.jpa;

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
        Video video = new Video(
                "Test Video",
                "testuser",
                "video.mp4",
                "thumbnail.webp"
        );

        VideoEntity entity = mapper.toEntity(video);

        assertNotNull(entity);
        assertEquals(video.getTitle(), entity.getTitle());
        assertEquals(video.getUser(), entity.getUser());
        assertEquals(video.getVideoFileName(), entity.getVideoFileName());
        assertEquals(video.getThumbnailFileName(), entity.getThumbnailFileName());
    }

    @Test
    void testToDomain() {
        VideoEntity entity = new VideoEntity(
                UUID.randomUUID(),
                "Test Video",
                "testuser",
                "video.mp4",
                "thumbnail.webp"
        );

        Video video = mapper.toDomain(entity);

        assertNotNull(video);
        assertEquals(entity.getTitle(), video.getTitle());
        assertEquals(entity.getUser(), video.getUser());
        assertEquals(entity.getVideoFileName(), video.getVideoFileName());
        assertEquals(entity.getThumbnailFileName(), video.getThumbnailFileName());
    }

    @Test
    void testRoundTripMapping() {
        Video originalVideo = new Video(
                "Original Video",
                "originaluser",
                "original.mp4",
                "original.webp"
        );

        VideoEntity entity = mapper.toEntity(originalVideo);
        Video mappedVideo = mapper.toDomain(entity);

        assertEquals(originalVideo.getTitle(), mappedVideo.getTitle());
        assertEquals(originalVideo.getUser(), mappedVideo.getUser());
        assertEquals(originalVideo.getVideoFileName(), mappedVideo.getVideoFileName());
        assertEquals(originalVideo.getThumbnailFileName(), mappedVideo.getThumbnailFileName());
    }
}
