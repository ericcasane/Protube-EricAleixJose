package com.tecnocampus.LS2.protube_back.controller;

import com.tecnocampus.LS2.protube_back.services.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@Tag(name = "Videos", description = "Endpoints for videos")
public class VideosRestController {

    @Autowired
    VideoService videoService;

    @GetMapping("")
    @Operation(summary = "Get all videos")
    public ResponseEntity<List<String>> getVideos() {
        return ResponseEntity.ok().body(videoService.getVideos());

    }
}
