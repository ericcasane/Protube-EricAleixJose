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

    @Autowired
    VideoReactionService reactionService;

    @Autowired
    TemporaryCommentService temporaryCommentService;

    @Autowired
    UserJpaRepository userRepository;

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

        Video video = videos.stream()
                .filter(v -> v.getVideoFileName().equals(id + ".mp4") ||
                            v.getVideoFileName().startsWith(id + "."))
                .findFirst()
                .orElse(null);

        if (video == null) {
            return ResponseEntity.notFound().build();
        }

        UUID userId = null;
        String userReaction = null;
        try {
            if (authentication != null && authentication.getName() != null) {
                // In a real JWT setup, the subject might be the username or UUID strings.
                // We'll try to parse as UUID, if fails, we lookup by username.
                userId = getUserIdFromAuthentication(authentication);

                if (userId != null) {
                    VideoReactionResponseDTO reactionStats = reactionService.getReactionStats(userId, id);
                    userReaction = reactionStats.getUserReaction();
                }
            }
        } catch (Exception e) {
            // User is authenticated but ID extraction failed, ignore
        }

        return ResponseEntity.ok(mapVideoToDetailResponse(video, userId, userReaction));
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "Add or toggle like on video")
    public ResponseEntity<VideoReactionResponseDTO> likeVideo(
            @PathVariable String id,
            Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        UUID userId = getUserIdFromAuthentication(authentication);
        if (userId == null) {
            return ResponseEntity.status(400).body(null);
        }

        VideoReactionResponseDTO response = reactionService.addLike(userId, id);
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

        UUID userId = getUserIdFromAuthentication(authentication);
        if (userId == null) {
            return ResponseEntity.status(400).body(null);
        }

        VideoReactionResponseDTO response = reactionService.addDislike(userId, id);
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

        UUID userId = getUserIdFromAuthentication(authentication);
        if (userId == null) {
            return ResponseEntity.status(400).body(null);
        }

        VideoReactionResponseDTO response = reactionService.removeReaction(userId, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/reactions")
    @Operation(summary = "Get video reactions count and user reaction")
    public ResponseEntity<VideoReactionResponseDTO> getVideoReactions(
            @PathVariable String id,
            Authentication authentication) {
        UUID userId = null;
        if (authentication != null) {
            userId = getUserIdFromAuthentication(authentication);
        }

        VideoReactionResponseDTO response = reactionService.getReactionStats(userId, id);
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

        String username = authentication.getName(); // Assuming username is in auth name

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

    private UUID getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        String principal = authentication.getName();
        try {
            // Try explicit UUID
            return UUID.fromString(principal);
        } catch (IllegalArgumentException e) {
            // Look up by username if not a UUID
            return userRepository.findByUsername(principal)
                    .map(UserEntity::getId)
                    .orElse(null);
        }
    }

    private VideoListResponseDTO mapVideoToListResponse(Video video) {
        String videoUrl = video.getVideoFileName();
        String thumbnailUrl = video.getThumbnailFileName();
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

        Long likesCount = video.getLikeCount() != null ? video.getLikeCount() : 0L;
        Long dislikesCount = video.getDislikeCount() != null ? video.getDislikeCount() : 0L;

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
    @GetMapping("/search")
    @Operation(summary = "Search videos by title, description or username")
    public ResponseEntity<org.springframework.data.domain.Page<VideoListResponseDTO>> searchVideos(
            @org.springframework.web.bind.annotation.RequestParam String query,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "0") int page,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "20") int size) {

        org.springframework.data.domain.Page<Video> videos = videoService.searchVideos(query, page, size);
        org.springframework.data.domain.Page<VideoListResponseDTO> responses = videos.map(this::mapVideoToListResponse);

        return ResponseEntity.ok(responses);
    }
}
