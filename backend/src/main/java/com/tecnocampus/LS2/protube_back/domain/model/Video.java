package com.tecnocampus.LS2.protube_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Video {
    private Long id;
    private String title;
    private String user;
    private String videoFileName;
    private String thumbnailFileName;
}
