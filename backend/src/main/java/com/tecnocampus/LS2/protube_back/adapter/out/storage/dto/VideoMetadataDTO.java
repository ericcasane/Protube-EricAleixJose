package com.tecnocampus.LS2.protube_back.adapter.out.storage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for deserializing video metadata from JSON files
 * Maps to the structure of video JSON files in the videos directory
 */
@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoMetadataDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("user")
    private String user;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("duration")
    private Double duration;

    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("meta")
    private MetaDTO meta;

    @Setter
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MetaDTO {
        @JsonProperty("description")
        private String description;

        @JsonProperty("categories")
        private List<String> categories;

        @JsonProperty("tags")
        private List<String> tags;

        @JsonProperty("view_count")
        private Long viewCount;

        @JsonProperty("like_count")
        private Long likeCount;

        @JsonProperty("channel")
        private String channel;

        @JsonProperty("channel_follower_count")
        private Long channelFollowerCount;

        @JsonProperty("comments")
        private List<CommentMetaDTO> comments;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommentMetaDTO {
        @JsonProperty("text")
        private String text;

        @JsonProperty("author")
        private String author;

        @JsonProperty("timestamp")
        private Long timestamp;

        @JsonProperty("like_count")
        private Long likeCount;
    }
}
