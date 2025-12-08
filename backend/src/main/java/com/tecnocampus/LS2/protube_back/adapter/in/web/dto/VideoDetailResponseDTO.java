package com.tecnocampus.LS2.protube_back.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VideoDetailResponseDTO {
    private String id;
    private String title;
    private String videoUrl;
    private String thumbnailUrl;
    private Double duration;
    private String description;
    private Long viewCount;
    private Long likeCount;
    private Long timestamp;
    private ChannelDTO channel;
    private List<CommentDTO> comments;
}
