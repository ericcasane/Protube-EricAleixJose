package com.tecnocampus.LS2.protube_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VideoReaction {
    private UUID id;
    private Long userId;
    private UUID videoId;
    private ReactionType reactionType;

    public enum ReactionType {
        LIKE,
        DISLIKE
    }
}
