package com.decagon.blogRESTapi.services;

import com.decagon.blogRESTapi.dto.PostDto;
import com.decagon.blogRESTapi.entities.PostEntity;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto userDto);
    List<PostDto> getPosts(int page, int limit);

    PostDto getPostById(String postId);

    PostDto updatePost(String postId, PostDto userDto);
    void deletePost (String postId);
}
