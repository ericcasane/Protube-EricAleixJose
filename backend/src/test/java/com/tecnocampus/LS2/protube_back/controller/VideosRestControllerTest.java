package com.tecnocampus.LS2.protube_back.controller;

import com.tecnocampus.LS2.protube_back.adapter.in.web.VideosRestController;
import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.VideoResponse;
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
        Video video1 = new Video("video 1", "user1", "video1.mp4", "thumb1.webp");
        Video video2 = new Video("video 2", "user2", "video2.mp4", "thumb2.webp");
        List<Video> videos = List.of(video1, video2);

        when(videoService.getVideos()).thenReturn(videos);

        ResponseEntity<List<VideoResponse>> response = videosRestController.getVideos();
        List<VideoResponse> body = response.getBody();

        assertNotNull(body);
        assertEquals(2, body.size());

        VideoResponse response1 = body.get(0);
        assertEquals("video 1", response1.getTitle());
        assertEquals("user1", response1.getUser());
        assertTrue(response1.getVideoUrl().endsWith("/video1.mp4"));
        assertTrue(response1.getThumbnailUrl().endsWith("/thumb1.webp"));

        VideoResponse response2 = body.get(1);
        assertEquals("video 2", response2.getTitle());
        assertEquals("user2", response2.getUser());
        assertTrue(response2.getVideoUrl().endsWith("/video2.mp4"));
        assertTrue(response2.getThumbnailUrl().endsWith("/thumb2.webp"));
    }
}