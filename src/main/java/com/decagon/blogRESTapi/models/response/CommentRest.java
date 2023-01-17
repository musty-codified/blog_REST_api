package com.decagon.blogRESTapi.models.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRest {
    private String commentId;
    private String name;
    private String email;
    private String body;
}
