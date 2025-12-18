package com.tecnocampus.LS2.protube_back.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.*;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.UserEntity;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository.UserJpaRepository;
import com.tecnocampus.LS2.protube_back.domain.model.Channel;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.domain.service.TemporaryCommentService;
import com.tecnocampus.LS2.protube_back.domain.service.VideoReactionService;
import com.tecnocampus.LS2.protube_back.domain.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.tecnocampus.LS2.protube_back.configuration.SecurityConfig;
import com.tecnocampus.LS2.protube_back.adapter.in.web.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(VideosRestController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@AutoConfigureMockMvc // Enable security filters
public class VideosRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoService videoService;

    @MockBean
    private VideoReactionService reactionService;

    @MockBean
    private TemporaryCommentService temporaryCommentService;

    @MockBean
    private UserJpaRepository userRepository;

    @MockBean
    private com.tecnocampus.LS2.protube_back.adapter.out.security.component.JwtTokenProvider jwtTokenProvider;

    @MockBean
    private com.tecnocampus.LS2.protube_back.adapter.out.security.component.CustomUserDetailsService customUserDetailsService;



    private Video sampleVideo;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        Channel channel = new Channel();
        channel.setName("Test Channel");
        channel.setFollowerCount(100L);

        sampleVideo = new Video();
        sampleVideo.setVideoFileName("video123.mp4");
        sampleVideo.setTitle("Test Video");
        sampleVideo.setThumbnailFileName("thumb.jpg");
        sampleVideo.setDuration(120.0);
        sampleVideo.setViewCount(1000L);
        sampleVideo.setTimestamp(System.currentTimeMillis());
        sampleVideo.setChannel(channel);
        sampleVideo.setDescription("Test Description");
        sampleVideo.setLikeCount(10L);
        sampleVideo.setDislikeCount(2L);
    }

    @Test
    void getVideos_ShouldReturnListOfVideos() throws Exception {
        when(videoService.getVideos()).thenReturn(Collections.singletonList(sampleVideo));

        mockMvc.perform(get("/api/videos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Video"))
                .andExpect(jsonPath("$[0].id").value("video123")); // .replaceFirst("\\.mp4$", "")
    }

    @Test
    void getVideoById_WhenVideoExists_ShouldReturnVideoDetail() throws Exception {
        when(videoService.getVideos()).thenReturn(Collections.singletonList(sampleVideo));
        when(reactionService.getReactionStats(any(), anyString())).thenReturn(new VideoReactionResponseDTO(10L, 2L, null));

        mockMvc.perform(get("/api/videos/video123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Video"))
                .andExpect(jsonPath("$.id").value("video123"));
    }

    @Test
    void getVideoById_WhenVideoDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(videoService.getVideos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/videos/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getVideoById_WithAuth_ShouldReturnUserReaction() throws Exception {
        when(videoService.getVideos()).thenReturn(Collections.singletonList(sampleVideo));
        
        // Mocking user lookup via UUID string in auth name
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        
        VideoReactionResponseDTO reactionResponse = new VideoReactionResponseDTO(10L, 2L, "LIKE");
        when(reactionService.getReactionStats(any(UUID.class), eq("video123"))).thenReturn(reactionResponse);

        // Create proper Authentication object
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId.toString(), null, Collections.emptyList());

        // Simulate Authenticated user with UUID as name
        mockMvc.perform(get("/api/videos/video123")
                .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userReaction").value("LIKE"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void likeVideo_ShouldReturnUpdatedStats() throws Exception {
        // Mock user lookup
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));

        VideoReactionResponseDTO reactionResponse = new VideoReactionResponseDTO(11L, 2L, "LIKE");
        when(reactionService.addLike(any(UUID.class), eq("video123"))).thenReturn(reactionResponse);

        mockMvc.perform(post("/api/videos/video123/like")
                .with(csrf())) // csrf might be needed if security is active, though disabled by filter config above
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likesCount").value(11))
                .andExpect(jsonPath("$.userReaction").value("LIKE"));
    }

    @Test
    void likeVideo_NoAuth_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/videos/video123/like").with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    void dislikeVideo_ShouldReturnUpdatedStats() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));


        when(reactionService.addDislike(any(UUID.class), eq("video123"))).thenReturn(new VideoReactionResponseDTO(10L, 3L, "DISLIKE"));

        mockMvc.perform(post("/api/videos/video123/dislike").with(csrf())
                .principal(() -> userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dislikesCount").value(3));
    }

    @Test
    @WithMockUser(username = "testuser")
    void removeReaction_ShouldReturnUpdatedStats() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));


        when(reactionService.removeReaction(any(UUID.class), eq("video123"))).thenReturn(new VideoReactionResponseDTO(10L, 2L, null));

        mockMvc.perform(delete("/api/videos/video123/reaction").with(csrf())
                .principal(() -> userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userReaction").doesNotExist());
    }

    @Test
    @WithMockUser(username = "testuser")
    void addComment_ShouldReturnNewComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO("testuser", "Great video!", System.currentTimeMillis(), 0L);
        when(temporaryCommentService.addComment(eq("video123"), eq("testuser"), eq("Great video!")))
                .thenReturn(commentDTO);

        mockMvc.perform(post("/api/videos/video123/comments").with(csrf())
                .principal(() -> "testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\":\"Great video!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Great video!"))
                .andExpect(jsonPath("$.author").value("testuser"));
    }

    @Test
    void getComments_ShouldReturnListOfComments() throws Exception {
        CommentDTO commentDTO = new CommentDTO("user1", "Nice", System.currentTimeMillis(), 5L);
        when(temporaryCommentService.getComments("video123")).thenReturn(Collections.singletonList(commentDTO));

        mockMvc.perform(get("/api/videos/video123/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("Nice"));
    }

    // Additional tests for search can be added here
}
