package com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.repository;

import com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity.VideoReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoReactionJpaRepository extends JpaRepository<VideoReactionEntity, UUID> {
    
    Optional<VideoReactionEntity> findByUserIdAndVideoId(Long userId, UUID videoId);
    
    @Query("SELECT COUNT(vr) FROM VideoReactionEntity vr WHERE vr.video.id = :videoId AND vr.reactionType = 'LIKE'")
    Long countLikesByVideoId(@Param("videoId") UUID videoId);
    
    @Query("SELECT COUNT(vr) FROM VideoReactionEntity vr WHERE vr.video.id = :videoId AND vr.reactionType = 'DISLIKE'")
    Long countDislikesByVideoId(@Param("videoId") UUID videoId);
    
    void deleteByUserIdAndVideoId(Long userId, UUID videoId);
}
