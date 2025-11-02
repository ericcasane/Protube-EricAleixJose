package com.tecnocampus.LS2.protube_back.adapter.out.storage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for deserializing video metadata from JSON files
 * Maps to the structure of video JSON files in the videos directory
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoMetadataDTO {

    // Getters and setters
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

}
