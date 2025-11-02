package com.tecnocampus.LS2.protube_back.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "videos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VideoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "username", nullable = false)
    private String user;

    @Column(nullable = false)
    private String videoFileName;

    @Column(nullable = false)
    private String thumbnailFileName;

    public VideoEntity(String title, String user, String videoFileName, String thumbnailFileName) {
        this.title = title;
        this.user = user;
        this.videoFileName = videoFileName;
        this.thumbnailFileName = thumbnailFileName;
    }
}
