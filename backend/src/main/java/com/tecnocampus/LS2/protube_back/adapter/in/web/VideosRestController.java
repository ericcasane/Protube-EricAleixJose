package com.tecnocampus.LS2.protube_back.adapter.in.web;

import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.*;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.domain.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/videos")
@Tag(name = "Videos", description = "Endpoints for videos")
public class VideosRestController {

    @Autowired
    VideoService videoService;

    @GetMapping("")
    @Operation(summary = "Get all videos with basic information for list view")
    public ResponseEntity<List<VideoListResponseDTO>> getVideos() {
        List<Video> videos = videoService.getVideos();
        List<VideoListResponseDTO> responses = videos.stream()
                .map(this::mapVideoToListResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get complete video details including comments")
    public ResponseEntity<VideoDetailResponseDTO> getVideoById(@PathVariable String id) {
        List<Video> videos = videoService.getVideos();

        Video video = videos.stream()
                .filter(v -> v.getVideoFileName().startsWith(id))
                .findFirst()
                .orElse(null);

        if (video == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mapVideoToDetailResponse(video));
    }

    private VideoListResponseDTO mapVideoToListResponse(Video video) {
        String videoUrl = "/videos/" + video.getVideoFileName();
        String thumbnailUrl = "/videos/" + video.getThumbnailFileName();
        String channelName = video.getChannel() != null ? video.getChannel().getName() : video.getUser();

        return new VideoListResponseDTO(
                video.getVideoFileName().replaceFirst("\\.mp4$", ""),
                video.getTitle(),
                channelName,
                videoUrl,
                thumbnailUrl,
                video.getDuration(),
                video.getViewCount(),
                video.getTimestamp()
        );
    }

    private VideoDetailResponseDTO mapVideoToDetailResponse(Video video) {
        String videoUrl = "/videos/" + video.getVideoFileName();
        String thumbnailUrl = "/videos/" + video.getThumbnailFileName();

        ChannelDTO channelDTO = null;
        if (video.getChannel() != null) {
            channelDTO = new ChannelDTO(
                    video.getChannel().getName(),
                    video.getChannel().getFollowerCount()
            );
        }

        List<CommentDTO> commentDTOs = null;
        if (video.getComments() != null) {
            commentDTOs = video.getComments().stream()
                    .map(comment -> new CommentDTO(
                            comment.getAuthor(),
                            comment.getText(),
                            comment.getTimestamp(),
                            comment.getLikeCount()
                    ))
                    .collect(Collectors.toList());
        }

        return new VideoDetailResponseDTO(
                video.getVideoFileName().replaceFirst("\\.mp4$", ""),
                video.getTitle(),
                videoUrl,
                thumbnailUrl,
                video.getDuration(),
                video.getDescription(),
                video.getViewCount(),
                video.getLikeCount(),
                video.getTimestamp(),
                channelDTO,
                commentDTOs
        );
    }
}
