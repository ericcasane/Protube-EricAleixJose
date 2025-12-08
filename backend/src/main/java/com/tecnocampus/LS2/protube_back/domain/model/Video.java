package com.tecnocampus.LS2.protube_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Video {
    private String title;
    private String user;
    private String videoFileName;
    private String thumbnailFileName;
    private Double duration;
    private String description;
    private Long timestamp;
    private Long viewCount;
    private Long likeCount;
    private Channel channel;
    private List<Comment> comments;
}
