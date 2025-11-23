package com.tecnocampus.LS2.protube_back.adapter.in.web;

import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.*;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.domain.service.VideoService;
import com.tecnocampus.LS2.protube_back.domain.service.TemporaryVideoReactionService;
import com.tecnocampus.LS2.protube_back.domain.service.TemporaryCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/videos")
@Tag(name = "Videos", description = "Endpoints for videos")
public class VideosRestController {

    @Autowired
    VideoService videoService;

    @Autowired
    TemporaryVideoReactionService temporaryReactionService;

    @Autowired
    TemporaryCommentService temporaryCommentService;

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
    public ResponseEntity<VideoDetailResponseDTO> getVideoById(
            @PathVariable String id,
            Authentication authentication) {
        List<Video> videos = videoService.getVideos();

        // Match by filename (e.g., "0" matches "0.mp4")
        Video video = videos.stream()
                .filter(v -> v.getVideoFileName().equals(id + ".mp4") || 
                            v.getVideoFileName().startsWith(id + "."))
                .findFirst()
                .orElse(null);

        if (video == null) {
            return ResponseEntity.notFound().build();
        }

        Long userId = null;
        try {
            if (authentication != null && authentication.getName() != null) {
                userId = Long.parseLong(authentication.getName());
            }
        } catch (NumberFormatException e) {
            // User is authenticated but ID is not numeric, ignore
        }
        
        return ResponseEntity.ok(mapVideoToDetailResponse(video, userId));
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "Add or toggle like on video")
    public ResponseEntity<VideoReactionResponseDTO> likeVideo(
            @PathVariable String id,
            Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        Long userId;
        try {
            userId = Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(null);
        }

        VideoReactionResponseDTO response = temporaryReactionService.addLike(userId, id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/dislike")
    @Operation(summary = "Add or toggle dislike on video")
    public ResponseEntity<VideoReactionResponseDTO> dislikeVideo(
            @PathVariable String id,
            Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        Long userId;
        try {
            userId = Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(null);
        }

        VideoReactionResponseDTO response = temporaryReactionService.addDislike(userId, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/reaction")
    @Operation(summary = "Remove reaction from video")
    public ResponseEntity<VideoReactionResponseDTO> removeReaction(
            @PathVariable String id,
            Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        Long userId;
        try {
            userId = Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(null);
        }

        VideoReactionResponseDTO response = temporaryReactionService.removeReaction(userId, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/reactions")
    @Operation(summary = "Get video reactions count and user reaction")
    public ResponseEntity<VideoReactionResponseDTO> getVideoReactions(
            @PathVariable String id,
            Authentication authentication) {
        Long userId = null;
        if (authentication != null) {
            try {
                userId = Long.parseLong(authentication.getName());
            } catch (NumberFormatException e) {
                // Ignore, user is authenticated but ID is not numeric
            }
        }

        VideoReactionResponseDTO response = temporaryReactionService.getReactionStats(userId, id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Add a comment to a video")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable String id,
            @RequestBody CommentCreateDTO commentCreateDTO,
            Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        // Get username from authentication (we need to extract it from JWT claims)
        // For now, we'll use a workaround to get the username
        String username = getUsernameFromAuthentication(authentication);
        
        if (username == null || commentCreateDTO.getText() == null || commentCreateDTO.getText().trim().isEmpty()) {
            return ResponseEntity.status(400).build();
        }

        CommentDTO newComment = temporaryCommentService.addComment(id, username, commentCreateDTO.getText());
        return ResponseEntity.ok(newComment);
    }

    @GetMapping("/{id}/comments")
    @Operation(summary = "Get all comments for a video")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable String id) {
        List<CommentDTO> comments = temporaryCommentService.getComments(id);
        return ResponseEntity.ok(comments);
    }

    private String getUsernameFromAuthentication(Authentication authentication) {
        // Since we're storing userId as the principal, we need to load the user to get username
        // For now, we'll use a simple mapping based on userId
        // In a real app, you'd query the database
        try {
            Long userId = Long.parseLong(authentication.getName());
            // Temporary mapping - in production, query user service
            if (userId == 1L) return "jmartorell";
            if (userId == 2L) return "ealeix";
            if (userId == 3L) return "jcasane";
            return "user" + userId;
        } catch (Exception e) {
            return null;
        }
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

    private VideoDetailResponseDTO mapVideoToDetailResponse(Video video, Long userId) {
        String videoUrl = "/videos/" + video.getVideoFileName();
        String thumbnailUrl = "/videos/" + video.getThumbnailFileName();
        String videoIdString = video.getVideoFileName().replaceFirst("\\.mp4$", "");

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

        // For like/dislike functionality, we'll use a simple counter based on the video data
        // Since we don't have a real UUID-based system yet, we'll return defaults
        Long likesCount = video.getLikeCount() != null ? video.getLikeCount() : 0L;
        Long dislikesCount = 0L; // Default for now
        String userReaction = null; // Default for now - can be implemented later with proper ID system

        return new VideoDetailResponseDTO(
                videoIdString,
                video.getTitle(),
                videoUrl,
                thumbnailUrl,
                video.getDuration(),
                video.getDescription(),
                video.getViewCount(),
                likesCount,
                dislikesCount,
                userReaction,
                video.getTimestamp(),
                channelDTO,
                commentDTOs
        );
    }
}
