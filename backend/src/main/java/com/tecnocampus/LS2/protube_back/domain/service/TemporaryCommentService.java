package com.tecnocampus.LS2.protube_back.domain.service;

import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.CommentDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TemporaryCommentService {
    // Map: videoId -> List of comments
    private final Map<String, List<CommentDTO>> videoComments = new ConcurrentHashMap<>();
    private final AtomicLong commentIdCounter = new AtomicLong(1000);

    /**
     * Add a new comment to a video
     * @param videoId The video ID
     * @param username The username of the commenter
     * @param text The comment text
     * @return The created comment
     */
    public CommentDTO addComment(String videoId, String username, String text) {
        CommentDTO newComment = new CommentDTO(
                username,
                text,
                System.currentTimeMillis() / 1000, // Unix timestamp in seconds
                0L // Initial like count
        );

        videoComments.computeIfAbsent(videoId, k -> new ArrayList<>()).add(newComment);
        
        return newComment;
    }

    /**
     * Get all comments for a video
     * @param videoId The video ID
     * @return List of comments
     */
    public List<CommentDTO> getComments(String videoId) {
        return new ArrayList<>(videoComments.getOrDefault(videoId, new ArrayList<>()));
    }
}
