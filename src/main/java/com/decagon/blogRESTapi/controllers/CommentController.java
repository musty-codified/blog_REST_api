package com.decagon.blogRESTapi.controllers;


import com.decagon.blogRESTapi.dto.CommentDto;
import com.decagon.blogRESTapi.exceptions.ResourceNotFoundException;
import com.decagon.blogRESTapi.models.request.CommentRequestModel;
import com.decagon.blogRESTapi.models.response.*;
import com.decagon.blogRESTapi.services.CommentService;
import com.decagon.blogRESTapi.services.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/") //http://localhost:8090/api/posts
public class CommentController {
    private final ModelMapper modelMapper;
    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentRest createComment(@Valid @RequestBody CommentRequestModel commentDetails,
                                     @PathVariable(value = "postId") String postId) {

        CommentRest returnValue = new CommentRest();
        if (commentDetails.getName() == null)
            throw new ResourceNotFoundException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        CommentDto commentDto = modelMapper.map(commentDetails, CommentDto.class);

        CommentDto createdComment = commentService.createComment(postId, commentDto);
        BeanUtils.copyProperties(createdComment, CommentRest.class);
        return returnValue;
    }


    @GetMapping("/posts/{postId}/comments")
    public List<CommentRest> getComments(@PathVariable(value = "postId") String id) {
        List<CommentRest> returnValue = new ArrayList<>();
        List<CommentDto> commentDto = commentService.getComments(id);
        if (commentDto != null && !commentDto.isEmpty()) {
            Type listType = new TypeToken<List<CommentRest>>() {
            }.getType();
            returnValue = modelMapper.map(commentDto, listType);
        }
        return returnValue;
    }
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public CommentRest getComment(@PathVariable String postId, @PathVariable long commentId){
        CommentDto commentDto = commentService.getComment(postId, commentId);
        CommentRest returnValue = modelMapper.map(commentDto, CommentRest.class);
        return returnValue;
}
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public CommentRest updateComment(@PathVariable String postId,
                                     @PathVariable long commentId, @Valid @RequestBody CommentRequestModel commentDetails){
        CommentRest returnValue = new CommentRest();
        CommentDto commentDto = new CommentDto();
        BeanUtils.copyProperties(commentDetails, commentDto);
        CommentDto updatedComment = commentService.updateComment(postId, commentId, commentDto);
        BeanUtils.copyProperties(updatedComment, returnValue);
        return returnValue;
    }

    @DeleteMapping(path = "posts/{postId}/comments/{commentId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public OperationStatusModel deleteComment(@PathVariable String postId, @PathVariable long commentId){
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        commentService.deleteComment(postId, commentId);
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }
}