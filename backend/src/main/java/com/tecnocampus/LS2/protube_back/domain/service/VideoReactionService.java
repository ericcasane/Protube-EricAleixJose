package com.tecnocampus.LS2.protube_back.domain.service;

import com.tecnocampus.LS2.protube_back.adapter.in.web.dto.VideoReactionResponseDTO;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.UserEntity;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.VideoEntity;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.VideoReactionEntity;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository.UserJpaRepository;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository.VideoJpaRepository;
import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository.VideoReactionJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VideoReactionService {

    private final VideoReactionJpaRepository videoReactionRepository;
    private final VideoJpaRepository videoRepository;
    private final UserJpaRepository userRepository;

    public VideoReactionService(VideoReactionJpaRepository videoReactionRepository,
                                VideoJpaRepository videoRepository,
                                UserJpaRepository userRepository) {
        this.videoReactionRepository = videoReactionRepository;
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VideoReactionResponseDTO addLike(UUID userId, String videoIdString) {
        VideoEntity video = getVideoEntity(videoIdString);
        return addOrUpdateReaction(userId, video, VideoReactionEntity.ReactionType.LIKE);
    }

    @Transactional
    public VideoReactionResponseDTO addDislike(UUID userId, String videoIdString) {
        VideoEntity video = getVideoEntity(videoIdString);
        return addOrUpdateReaction(userId, video, VideoReactionEntity.ReactionType.DISLIKE);
    }

    @Transactional
    public VideoReactionResponseDTO removeReaction(UUID userId, String videoIdString) {
        VideoEntity video = getVideoEntity(videoIdString);
        
        Optional<VideoReactionEntity> existingReaction = 
                videoReactionRepository.findByUserIdAndVideoId(userId, video.getId());

        if (existingReaction.isPresent()) {
            VideoReactionEntity reaction = existingReaction.get();
            updateVideoCounters(video, reaction.getReactionType(), null);
            videoReactionRepository.delete(reaction);
            videoRepository.save(video);
        }

        return getReactionStats(userId, video);
    }

    @Transactional
    public VideoReactionResponseDTO getReactionStats(UUID userId, String videoIdString) {
        VideoEntity video = getVideoEntity(videoIdString);
        return getReactionStats(userId, video);
    }

    private VideoReactionResponseDTO getReactionStats(UUID userId, VideoEntity video) {
        String userReaction = null;
        if (userId != null) {
            Optional<VideoReactionEntity> reaction = videoReactionRepository.findByUserIdAndVideoId(userId, video.getId());
            if (reaction.isPresent()) {
                userReaction = reaction.get().getReactionType().name();
            }
        }

        return new VideoReactionResponseDTO(
                video.getLikeCount() != null ? video.getLikeCount() : 0L,
                video.getDislikeCount() != null ? video.getDislikeCount() : 0L,
                userReaction
        );
    }
    
    // For direct access if needed
    public Optional<VideoReactionEntity.ReactionType> getUserReaction(UUID userId, UUID videoId) {
        return videoReactionRepository.findByUserIdAndVideoId(userId, videoId)
                .map(VideoReactionEntity::getReactionType);
    }

    private VideoReactionResponseDTO addOrUpdateReaction(UUID userId, VideoEntity video, VideoReactionEntity.ReactionType newType) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Optional<VideoReactionEntity> existingReactionWrapper = 
                videoReactionRepository.findByUserIdAndVideoId(userId, video.getId());

        if (existingReactionWrapper.isPresent()) {
            VideoReactionEntity reaction = existingReactionWrapper.get();
            VideoReactionEntity.ReactionType oldType = reaction.getReactionType();

            if (oldType == newType) {
                // Toggle off
                updateVideoCounters(video, oldType, null);
                videoReactionRepository.delete(reaction);
            } else {
                // Change reaction
                updateVideoCounters(video, oldType, newType);
                reaction.setReactionType(newType);
                videoReactionRepository.save(reaction);
            }
        } else {
            // New reaction
            updateVideoCounters(video, null, newType);
            VideoReactionEntity newReaction = new VideoReactionEntity();
            newReaction.setUser(user);
            newReaction.setVideo(video);
            newReaction.setReactionType(newType);
            videoReactionRepository.save(newReaction);
        }
        
        videoRepository.save(video);
        return getReactionStats(userId, video);
    }

    private void updateVideoCounters(VideoEntity video, VideoReactionEntity.ReactionType oldType, VideoReactionEntity.ReactionType newType) {
        long currentLikes = video.getLikeCount() != null ? video.getLikeCount() : 0L;
        long currentDislikes = video.getDislikeCount() != null ? video.getDislikeCount() : 0L;

        // Javascript-like logic: currentLikes = max(0, currentLikes) just in case
        currentLikes = Math.max(0, currentLikes);
        currentDislikes = Math.max(0, currentDislikes);

        // Remove old effect
        if (oldType == VideoReactionEntity.ReactionType.LIKE) {
            currentLikes--;
        } else if (oldType == VideoReactionEntity.ReactionType.DISLIKE) {
            currentDislikes--;
        }

        // Add new effect
        if (newType == VideoReactionEntity.ReactionType.LIKE) {
            currentLikes++;
        } else if (newType == VideoReactionEntity.ReactionType.DISLIKE) {
            currentDislikes++;
        }

        video.setLikeCount(currentLikes);
        video.setDislikeCount(currentDislikes);
    }

    private VideoEntity getVideoEntity(String videoIdString) {
        // Try finding by filename (assuming .mp4 suffix logic from controller)
        // If the ID passed doesn't have .mp4, append it.
        String filename = videoIdString.endsWith(".mp4") ? videoIdString : videoIdString + ".mp4";
        
        return videoRepository.findByVideoFileName(filename)
                .or(() -> videoRepository.findByVideoFileName(videoIdString)) // Try exact match just in case
                .orElseThrow(() -> new RuntimeException("Video not found: " + videoIdString));
    }
}
