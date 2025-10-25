package com.tecnocampus.LS2.protube_back.controller;

import com.tecnocampus.LS2.protube_back.controller.dto.VideoResponse;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.services.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/videos")
@Tag(name = "Videos", description = "Endpoints for videos")
public class VideosRestController {

    @Autowired
    VideoService videoService;

    @GetMapping("")
    @Operation(summary = "Get all videos")
    public ResponseEntity<List<VideoResponse>> getVideos() {
        List<Video> videos = videoService.getVideos();
        List<VideoResponse> responses = videos.stream()
                .map(this::mapVideoToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    private VideoResponse mapVideoToResponse(Video video) {
        String videoUrl = "/videos/" + video.getVideoFileName();
        String thumbnailUrl = "/videos/" + video.getThumbnailFileName();

        return new VideoResponse(
                video.getTitle(),
                video.getUser(),
                videoUrl,
                thumbnailUrl
        );
    }
}
