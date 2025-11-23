package com.tecnocampus.LS2.protube_back.domain.service;

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
    public void addLike(Long userId, UUID videoId) {
        addOrUpdateReaction(userId, videoId, VideoReactionEntity.ReactionType.LIKE);
    }

    @Transactional
    public void addDislike(Long userId, UUID videoId) {
        addOrUpdateReaction(userId, videoId, VideoReactionEntity.ReactionType.DISLIKE);
    }

    @Transactional
    public void removeReaction(Long userId, UUID videoId) {
        videoReactionRepository.deleteByUserIdAndVideoId(userId, videoId);
    }

    @Transactional
    public void addOrUpdateReaction(Long userId, UUID videoId, VideoReactionEntity.ReactionType reactionType) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        VideoEntity video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        Optional<VideoReactionEntity> existingReaction = 
                videoReactionRepository.findByUserIdAndVideoId(userId, videoId);

        if (existingReaction.isPresent()) {
            VideoReactionEntity reaction = existingReaction.get();
            if (reaction.getReactionType() == reactionType) {
                // Si ya tiene la misma reacción, la eliminamos (toggle)
                videoReactionRepository.delete(reaction);
            } else {
                // Si tiene una reacción diferente, la cambiamos
                reaction.setReactionType(reactionType);
                videoReactionRepository.save(reaction);
            }
        } else {
            // Si no tiene reacción, creamos una nueva
            VideoReactionEntity newReaction = new VideoReactionEntity();
            newReaction.setUser(user);
            newReaction.setVideo(video);
            newReaction.setReactionType(reactionType);
            videoReactionRepository.save(newReaction);
        }
    }

    public Optional<VideoReactionEntity.ReactionType> getUserReaction(Long userId, UUID videoId) {
        return videoReactionRepository.findByUserIdAndVideoId(userId, videoId)
                .map(VideoReactionEntity::getReactionType);
    }

    public Long getLikesCount(UUID videoId) {
        return videoReactionRepository.countLikesByVideoId(videoId);
    }

    public Long getDislikesCount(UUID videoId) {
        return videoReactionRepository.countDislikesByVideoId(videoId);
    }
}
