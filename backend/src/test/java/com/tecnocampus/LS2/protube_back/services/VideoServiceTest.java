package com.tecnocampus.LS2.protube_back.services;

import com.tecnocampus.LS2.protube_back.application.port.out.VideoRepository;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    VideoRepository videoRepository;

    @InjectMocks
    VideoService videoService;


    @Test
    void shouldGoToFolderVideos() {
        Video video1 = new Video("video1", "user1", "video1.mp4", "thumb1.jpg");
        Video video2 = new Video("video2", "user2", "video2.mp4", "thumb2.jpg");
        List<Video> videos = List.of(video1, video2);

        when(videoRepository.findAll()).thenReturn(videos);

        assertEquals(videos, videoService.getVideos());
    }

}