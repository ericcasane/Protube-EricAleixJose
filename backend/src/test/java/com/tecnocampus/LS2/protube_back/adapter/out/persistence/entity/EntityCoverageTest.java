package com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity;

import com.tecnocampus.LS2.protube_back.domain.model.VideoReaction;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EntityCoverageTest {

    @Test
    void videoEntityCoverage() {
        VideoEntity entity = new VideoEntity("title", "user", "file.mp4", "thumb.jpg");
        assertNotNull(entity);
        assertEquals("title", entity.getTitle());
        assertEquals("user", entity.getUser());
        assertEquals(0L, entity.getLikeCount());

        entity.setId(UUID.randomUUID());
        assertNotNull(entity.getId());

        entity.setDuration(10.0);
        assertEquals(10.0, entity.getDuration());

        entity.setDescription("desc");
        assertEquals("desc", entity.getDescription());

        entity.setTimestamp(100L);
        assertEquals(100L, entity.getTimestamp());

        entity.setViewCount(5L);
        assertEquals(5L, entity.getViewCount());
    }

    @Test
    void videoReactionCoverage() {
        VideoReaction reaction = new VideoReaction();
        reaction.setId(UUID.randomUUID());
        reaction.setUserId(1L);
        reaction.setVideoId(UUID.randomUUID());
        reaction.setReactionType(VideoReaction.ReactionType.LIKE);

        assertNotNull(reaction.getId());
        assertEquals(1L, reaction.getUserId());
        assertNotNull(reaction.getVideoId());
        assertEquals(VideoReaction.ReactionType.LIKE, reaction.getReactionType());

        VideoReaction reaction2 = new VideoReaction(UUID.randomUUID(), 2L, UUID.randomUUID(), VideoReaction.ReactionType.DISLIKE);
        assertEquals(VideoReaction.ReactionType.DISLIKE, reaction2.getReactionType());
    }

    @Test
    void reactionTypeCoverage() {
        assertEquals(VideoReaction.ReactionType.LIKE, VideoReaction.ReactionType.valueOf("LIKE"));
        assertNotNull(VideoReaction.ReactionType.values());
    }
}
