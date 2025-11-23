package com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "video_reactions", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "video_id"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VideoReactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private VideoEntity video;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType reactionType;

    public enum ReactionType {
        LIKE,
        DISLIKE
    }
}
