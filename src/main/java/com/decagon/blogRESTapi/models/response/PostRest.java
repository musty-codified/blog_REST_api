package com.decagon.blogRESTapi.models.response;


import com.decagon.blogRESTapi.models.request.CommentRequestModel;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class PostRest {
    private String postId;
    private String title;
    private String description;
    private String content;
    private Set<CommentRest> comments;

}
