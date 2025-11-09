package com.tecnocampus.LS2.protube_back.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDTO {
    private String author;
    private String text;
    private Long timestamp;
    private Long likeCount;
}
