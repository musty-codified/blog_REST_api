package com.decagon.blogRESTapi.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private long id;
    private String postId;
    private String title;
    private String description;
    private String content;
    private Set<CommentDto> comments;
}
