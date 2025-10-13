package com.tecnocampus.LS2.protube_back.domain.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Video {
    private String title;
    private String user;
    private String videoFileName;
    private String thumbnailFileName;
}
