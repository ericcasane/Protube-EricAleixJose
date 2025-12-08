package com.tecnocampus.LS2.protube_back.controller;

import com.tecnocampus.LS2.protube_back.adapter.in.web.VideosRestController;
import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.VideoListResponseDTO;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.domain.service.VideoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideosRestControllerTest {

    @InjectMocks
    VideosRestController videosRestController;

    @Mock
    VideoService videoService;

    @Test
    void getVideos() {
        Video video1 = new Video();
        video1.setTitle("video 1");
        video1.setUser("user1");
        video1.setVideoFileName("video1.mp4");
        video1.setThumbnailFileName("thumb1.webp");

        Video video2 = new Video();
        video2.setTitle("video 2");
        video2.setUser("user2");
        video2.setVideoFileName("video2.mp4");
        video2.setThumbnailFileName("thumb2.webp");

        List<Video> videos = List.of(video1, video2);

        when(videoService.getVideos()).thenReturn(videos);

        ResponseEntity<List<VideoListResponseDTO>> response = videosRestController.getVideos();
        List<VideoListResponseDTO> body = response.getBody();

        assertNotNull(body);
        assertEquals(2, body.size());

        VideoListResponseDTO response1 = body.get(0);
        assertEquals("video 1", response1.getTitle());
        assertEquals("user1", response1.getChannelName());
        assertTrue(response1.getVideoUrl().endsWith("/video1.mp4"));
        assertTrue(response1.getThumbnailUrl().endsWith("/thumb1.webp"));

        VideoListResponseDTO response2 = body.get(1);
        assertEquals("video 2", response2.getTitle());
        assertEquals("user2", response2.getChannelName());
        assertTrue(response2.getVideoUrl().endsWith("/video2.mp4"));
        assertTrue(response2.getThumbnailUrl().endsWith("/thumb2.webp"));
    }
}