package com.tecnocampus.LS2.protube_back.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Channel {
    private UUID id;
    private String name;
    private Long followerCount;
    private List<Video> videos;
}
