package com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "channels")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChannelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private Long followerCount;

    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY)
    private List<VideoEntity> videos;
}
