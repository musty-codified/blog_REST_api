package com.decagon.blogRESTapi.services;

import com.decagon.blogRESTapi.dto.CommentDto;

import java.util.List;

public interface CommentService {
   CommentDto createComment(String postId, CommentDto commentDto);
   List<CommentDto> getComments(String postId);

   CommentDto getComment(String postId, long commentId);

   CommentDto updateComment(String postId, long commentId, CommentDto commentDto);

   void deleteComment(String postId, long commentId);
}
