package com.tecnocampus.LS2.protube_back.infrastructure.adapter.out.persistence;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.adapter.VideoRepositoryAdapter;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.mapper.VideoEntityMapper;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.VideoEntity;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository.VideoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository.ChannelJpaRepository;

@ExtendWith(MockitoExtension.class)
class VideoRepositoryAdapterTest {

    @Mock
    private VideoJpaRepository jpaRepository;

    @Mock
    private ChannelJpaRepository channelJpaRepository;

    @Mock
    private VideoEntityMapper mapper;

    private VideoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new VideoRepositoryAdapter(jpaRepository, channelJpaRepository, mapper);
    }

    @Test
    void testSave() {
        Video video = new Video();
        video.setTitle("Test Video");
        video.setUser("testuser");
        video.setVideoFileName("video.mp4");
        video.setThumbnailFileName("thumbnail.webp");

        VideoEntity entity = new VideoEntity();
        entity.setId(UUID.randomUUID());
        entity.setTitle("Test Video");
        entity.setUser("testuser");
        entity.setVideoFileName("video.mp4");
        entity.setThumbnailFileName("thumbnail.webp");

        VideoEntity savedEntity = new VideoEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setTitle("Test Video");
        savedEntity.setUser("testuser");
        savedEntity.setVideoFileName("video.mp4");
        savedEntity.setThumbnailFileName("thumbnail.webp");
        
        Video savedVideo = new Video();
        savedVideo.setTitle("Test Video");
        savedVideo.setUser("testuser");
        savedVideo.setVideoFileName("video.mp4");
        savedVideo.setThumbnailFileName("thumbnail.webp");

        when(mapper.toEntity(video)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedVideo);

        Video result = adapter.save(video);

        assertNotNull(result);
        assertEquals(savedVideo, result);
        verify(mapper).toEntity(video);
        verify(jpaRepository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void testFindAll() {
        VideoEntity entity1 = new VideoEntity();
        entity1.setId(UUID.randomUUID());
        entity1.setTitle("Video 1");
        entity1.setUser("user1");
        entity1.setVideoFileName("video1.mp4");
        entity1.setThumbnailFileName("thumb1.webp");

        VideoEntity entity2 = new VideoEntity();
        entity2.setId(UUID.randomUUID());
        entity2.setTitle("Video 2");
        entity2.setUser("user2");
        entity2.setVideoFileName("video2.mp4");
        entity2.setThumbnailFileName("thumb2.webp");

        List<VideoEntity> entities = Arrays.asList(entity1, entity2);

        Video video1 = new Video();
        video1.setTitle("Video 1");
        video1.setUser("user1");
        video1.setVideoFileName("video1.mp4");
        video1.setThumbnailFileName("thumb1.webp");

        Video video2 = new Video();
        video2.setTitle("Video 2");
        video2.setUser("user2");
        video2.setVideoFileName("video2.mp4");
        video2.setThumbnailFileName("thumb2.webp");

        when(jpaRepository.findAll()).thenReturn(entities);
        when(mapper.toDomain(entity1)).thenReturn(video1);
        when(mapper.toDomain(entity2)).thenReturn(video2);

        List<Video> result = adapter.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(video1, result.get(0));
        assertEquals(video2, result.get(1));
        verify(jpaRepository).findAll();
        verify(mapper, times(2)).toDomain(any(VideoEntity.class));
    }

    @Test
    void testFindAllEmpty() {
        when(jpaRepository.findAll()).thenReturn(List.of());

        List<Video> result = adapter.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaRepository).findAll();
        verify(mapper, never()).toDomain(any(VideoEntity.class));
    }
}
