package com.tecnocampus.LS2.protube_back.services;

import com.tecnocampus.LS2.protube_back.adapter.out.in_memory.VideoRepository;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.domain.service.VideoService;
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
        Video video1 = new Video();
        video1.setTitle("video1");
        video1.setUser("user1");
        video1.setVideoFileName("video1.mp4");
        video1.setThumbnailFileName("thumb1.jpg");

        Video video2 = new Video();
        video2.setTitle("video2");
        video2.setUser("user2");
        video2.setVideoFileName("video2.mp4");
        video2.setThumbnailFileName("thumb2.jpg");

        List<Video> videos = List.of(video1, video2);

        when(videoRepository.findAll()).thenReturn(videos);

        assertEquals(videos, videoService.getVideos());
    }

}