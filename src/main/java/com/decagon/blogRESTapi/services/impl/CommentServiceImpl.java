package com.decagon.blogRESTapi.services.impl;
import com.decagon.blogRESTapi.dto.Utils;
import com.decagon.blogRESTapi.dto.CommentDto;
import com.decagon.blogRESTapi.entities.CommentEntity;
import com.decagon.blogRESTapi.entities.PostEntity;
import com.decagon.blogRESTapi.exceptions.BlogAPIException;
import com.decagon.blogRESTapi.exceptions.ResourceNotFoundException;
import com.decagon.blogRESTapi.models.response.ErrorMessages;
import com.decagon.blogRESTapi.repositories.CommentRepository;
import com.decagon.blogRESTapi.repositories.PostRepository;
import com.decagon.blogRESTapi.services.CommentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final ModelMapper modelMapper;

    private final Utils utils;
    @Override
    public CommentDto createComment(String postId, CommentDto commentDto) {

        CommentDto returnValue = new CommentDto();

        PostEntity postEntity = postRepository.findByPostId(postId)
                .orElseThrow(()->new ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setPostDetails(postEntity);
        String CommentId = utils.generatePostId(20);
        commentEntity.setCommentId(CommentId);
        commentEntity.setName(commentDto.getName());
        commentEntity.setEmail(commentDto.getEmail());
        commentEntity.setBody(commentDto.getBody());
        CommentEntity storedCommentDetails = commentRepository.save(commentEntity);
        BeanUtils.copyProperties(storedCommentDetails, CommentDto.class);
        return returnValue;
    }

    @Override
    public List<CommentDto> getComments(String postId) {
        List<CommentDto> returnValue = new ArrayList<>();
        PostEntity postEntity = postRepository.findByPostId(postId)
                .orElseThrow(()->new ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
//        if (commentEntity == null) return returnValue;

        Iterable<CommentEntity> comments = commentRepository.findAllByPostDetails(postEntity);
        for (CommentEntity commentEntity : comments) {
            returnValue.add(modelMapper.map(commentEntity, CommentDto.class));
        }

        return returnValue;

    }

    @Override
    public CommentDto getComment(String postId, long commentId) {
        CommentDto returnValue = null;
        PostEntity postEntity = postRepository.findByPostId(postId)
                .orElseThrow(()->new ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

       if(!commentEntity.getPostDetails().getPostId().equals(postEntity.getPostId())){

           throw new BlogAPIException("comment does not map to post");
       }
            returnValue = modelMapper.map(commentEntity, CommentDto.class);

        return returnValue;
    }

    @Override
    public CommentDto updateComment(String postId, long commentId, CommentDto commentDto) {
        CommentDto returnValue = new CommentDto();
        PostEntity postEntity = postRepository.findByPostId(postId)
                .orElseThrow(()->new ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        if(!commentEntity.getPostDetails().getPostId().equals(postEntity.getPostId())){

            throw new BlogAPIException(ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage());
        }

        commentEntity.setName(commentDto.getName());
        commentEntity.setEmail(commentDto.getEmail());
        commentEntity.setBody(commentDto.getBody());
        CommentEntity updatedCommentDetails = commentRepository.save(commentEntity);
        BeanUtils.copyProperties(updatedCommentDetails, returnValue);
        return returnValue;

    }

    @Override
    public void deleteComment(String postId, long commentId) {
        PostEntity postEntity = postRepository.findByPostId(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post with id: " + postId + " not found"));

        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment with id " + commentId + " not found"));
//        if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        commentRepository.delete(commentEntity);
    }
}

