package com.decagon.blogRESTapi.controllers;
import com.decagon.blogRESTapi.dto.PostDto;
import com.decagon.blogRESTapi.models.response.OperationStatusModel;
import com.decagon.blogRESTapi.models.request.PostDetailsRequestModel;
import com.decagon.blogRESTapi.models.response.PostRest;
import com.decagon.blogRESTapi.models.response.RequestOperationName;
import com.decagon.blogRESTapi.models.response.RequestOperationStatus;
import com.decagon.blogRESTapi.services.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/posts")
@AllArgsConstructor
public class PostController {

    private final ModelMapper modelMapper;
    private final PostService postService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostRest createPost(@Valid @RequestBody PostDetailsRequestModel postDetails) {

        PostRest returnValue = new PostRest();
        PostDto postDto = modelMapper.map(postDetails, PostDto.class);

        PostDto createdPost = postService.createPost(postDto);
        returnValue = modelMapper.map(createdPost, PostRest.class);

        return returnValue;

    }

    @GetMapping()
    public List<PostRest> getPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "limit", defaultValue = "1") int limit){
       // return postService.getPosts(page, limit);

        List<PostRest> returnValue = new ArrayList<>();

        List<PostDto> posts = postService.getPosts(page, limit);
        for (PostDto postDto : posts) {
            PostRest postModel = new PostRest();
            modelMapper.map(postDto, postModel);
            returnValue.add(postModel);
        }
        return returnValue;

    }

    @GetMapping("/{id}")
    public PostRest getPost(@PathVariable(name = "id") String postId) {
        PostRest returnValue = new PostRest();
        PostDto postDto = postService.getPostById(postId);
        BeanUtils.copyProperties(postDto, PostRest.class);
        return returnValue;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public PostRest updatePost (@PathVariable String id,  @Valid @RequestBody PostDetailsRequestModel postDetails){
        PostRest returnValue = new PostRest();
        PostDto postDto = new PostDto();
        BeanUtils.copyProperties(postDetails, postDto);
        PostDto updatedUser = postService.updatePost(id, postDto);
        BeanUtils.copyProperties(updatedUser, returnValue );
        return returnValue;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public OperationStatusModel deletePost(@PathVariable String id){
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        postService.deletePost(id);
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

}