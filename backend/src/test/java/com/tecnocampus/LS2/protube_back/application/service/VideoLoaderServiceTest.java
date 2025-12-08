package com.tecnocampus.LS2.protube_back.application.service;

import com.tecnocampus.LS2.protube_back.adapter.out.in_memory.VideoLoaderPort;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoLoaderServiceTest {

    @Mock
    private VideoLoaderPort videoLoaderPort;

    private VideoLoaderService service;

    @BeforeEach
    void setUp() {
        service = new VideoLoaderService(videoLoaderPort);
    }

    @Test
    void testLoadVideos() {
        Path videosDirectory = Paths.get("/test/videos");
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
        List<Video> expectedVideos = Arrays.asList(video1, video2);

        when(videoLoaderPort.loadVideosFromDirectory(videosDirectory)).thenReturn(expectedVideos);

        List<Video> result = service.loadVideos(videosDirectory);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedVideos, result);
        verify(videoLoaderPort).loadVideosFromDirectory(videosDirectory);
    }

    @Test
    void testLoadVideosEmpty() {
        Path videosDirectory = Paths.get("/test/videos");
        when(videoLoaderPort.loadVideosFromDirectory(videosDirectory)).thenReturn(List.of());

        List<Video> result = service.loadVideos(videosDirectory);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(videoLoaderPort).loadVideosFromDirectory(videosDirectory);
    }

    @Test
    void testDisplayVideosEmpty() {
        List<Video> emptyList = List.of();

        assertDoesNotThrow(() -> service.displayVideos(emptyList));
    }

    @Test
    void testDisplayVideosWithContent() {
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
        List<Video> videos = Arrays.asList(video1, video2);

        assertDoesNotThrow(() -> service.displayVideos(videos));
    }

    @Test
    void testDisplayVideosSingleVideo() {
        Video video = new Video();
        video.setTitle("Single Video");
        video.setUser("user");
        video.setVideoFileName("video.mp4");
        video.setThumbnailFileName("thumb.webp");
        List<Video> videos = List.of(video);

        assertDoesNotThrow(() -> service.displayVideos(videos));
    }
}
