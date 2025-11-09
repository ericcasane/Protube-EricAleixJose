package com.tecnocampus.LS2.protube_back.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VideoListResponseDTO {
    private String id;
    private String title;
    private String channelName;
    private String videoUrl;
    private String thumbnailUrl;
    private Double duration;
    private Long viewCount;
    private Long timestamp;
}
