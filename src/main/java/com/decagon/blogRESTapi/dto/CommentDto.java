package com.decagon.blogRESTapi.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private long id;
    private String postId;
    private String name;
    private String email;
    private String body;
    private PostDto postDetails;

}
