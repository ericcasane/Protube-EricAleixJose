package com.tecnocampus.LS2.protube_back.controller;

import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.services.VideoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideosRestControllerTest {

    @InjectMocks
    VideosRestController videosRestController;

    @Mock
    VideoService videoService;


    @Test
    void getVideos() {
        Video video1 = new Video("video 1", "user1", "video1.mp4", "thumb1.jpg");
        Video video2 = new Video("video 2", "user2", "video2.mp4", "thumb2.jpg");
        List<Video> videos = List.of(video1, video2);

        when(videoService.getVideos()).thenReturn(videos);
        assertEquals(videos, videosRestController.getVideos().getBody());
    }
}