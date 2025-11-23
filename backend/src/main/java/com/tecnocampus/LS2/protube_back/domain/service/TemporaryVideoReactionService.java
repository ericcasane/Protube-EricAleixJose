package com.tecnocampus.LS2.protube_back.domain.service;

import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.VideoReactionResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Temporary in-memory reaction service for videos identified by string IDs (filenames).
 * This is a workaround until the video system is migrated to use UUIDs.
 */
@Service
public class TemporaryVideoReactionService {

    // Map structure: videoId -> userId -> reactionType
    private final Map<String, Map<Long, String>> reactions = new ConcurrentHashMap<>();

    /**
     * Add or toggle a like reaction
     */
    public VideoReactionResponseDTO addLike(Long userId, String videoId) {
        return addOrUpdateReaction(userId, videoId, "LIKE");
    }

    /**
     * Add or toggle a dislike reaction
     */
    public VideoReactionResponseDTO addDislike(Long userId, String videoId) {
        return addOrUpdateReaction(userId, videoId, "DISLIKE");
    }

    /**
     * Remove reaction from video
     */
    public VideoReactionResponseDTO removeReaction(Long userId, String videoId) {
        reactions.computeIfAbsent(videoId, k -> new ConcurrentHashMap<>()).remove(userId);
        return getReactionStats(userId, videoId);
    }

    /**
     * Add or update reaction with toggle behavior (like YouTube)
     */
    private VideoReactionResponseDTO addOrUpdateReaction(Long userId, String videoId, String reactionType) {
        Map<Long, String> videoReactions = reactions.computeIfAbsent(videoId, k -> new ConcurrentHashMap<>());
        
        String existingReaction = videoReactions.get(userId);
        
        if (reactionType.equals(existingReaction)) {
            // Same reaction: toggle off (remove)
            videoReactions.remove(userId);
        } else {
            // Different or no reaction: set new reaction
            videoReactions.put(userId, reactionType);
        }
        
        return getReactionStats(userId, videoId);
    }

    /**
     * Get reaction statistics for a video
     */
    public VideoReactionResponseDTO getReactionStats(Long userId, String videoId) {
        Map<Long, String> videoReactions = reactions.getOrDefault(videoId, new ConcurrentHashMap<>());
        
        long likesCount = videoReactions.values().stream()
                .filter(r -> "LIKE".equals(r))
                .count();
        
        long dislikesCount = videoReactions.values().stream()
                .filter(r -> "DISLIKE".equals(r))
                .count();
        
        String userReaction = userId != null ? videoReactions.get(userId) : null;
        
        return new VideoReactionResponseDTO(likesCount, dislikesCount, userReaction);
    }
}
