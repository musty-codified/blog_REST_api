package com.decagon.blogRESTapi.services.impl;

import com.decagon.blogRESTapi.dto.Utils;
import com.decagon.blogRESTapi.dto.PostDto;
import com.decagon.blogRESTapi.entities.PostEntity;
import com.decagon.blogRESTapi.exceptions.ResourceNotFoundException;
import com.decagon.blogRESTapi.models.response.ErrorMessages;
import com.decagon.blogRESTapi.repositories.PostRepository;
import com.decagon.blogRESTapi.services.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private  final Utils utils;
    private final ModelMapper modelMapper;




    @Override
    public PostDto createPost(PostDto postDto) {

        PostEntity postEntity =  modelMapper.map(postDto, PostEntity.class);
        String publicPostId = utils.generatePostId(10);
        postEntity.setPostId(publicPostId);
        PostEntity storedPostDetails = postRepository.save(postEntity);
        PostDto returnValue=  modelMapper.map(storedPostDetails, PostDto.class);
        return returnValue;
    }

    @Override
    public List<PostDto> getPosts(int page, int limit) {
        List<PostDto> returnValue = new ArrayList<>();
        if(page>0) page = page-1;
        Pageable pageableRequest = PageRequest.of(page,limit);
        return postRepository.findAll(pageableRequest).map(postEntity -> PostDto.builder()
                        .title(postEntity.getTitle())
                        .postId(postEntity.getPostId())
                        .description(postEntity.getDescription())
                        .content(postEntity.getContent())
                        .build()).getContent();
//        List<PostEntity> posts=postPage.getContent();
//        return posts.stream()
//                .map(postEntity -> PostDto.builder()
//                        .title(postEntity.getTitle())
//                        .postId(postEntity.getPostId())
//                        .description(postEntity.getDescription())
//                        .content(postEntity.getContent())
//                        .build()
//                ).collect(Collectors.toList());

//        for(PostEntity postEntity:posts){
//            PostDto postDto = new PostDto();
//            BeanUtils.copyProperties(postEntity,postDto);
//            returnValue.add(postDto);
//        }
//
//        return returnValue;

    }

    @Override
    public PostDto getPostById(String postId) {

        PostDto returnValue = new PostDto();
        PostEntity postEntity = postRepository.findByPostId(postId).
                orElseThrow(()-> new ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
//        if(postEntity == null) throw new PostNotFoundException();
        BeanUtils.copyProperties(postEntity, PostDto.class);
        return returnValue;

    }

    @Override
    public PostDto updatePost(String postId, PostDto postDto) {
        PostDto returnValue = new PostDto();
        PostEntity postEntity = postRepository.findByPostId(postId)
                .orElseThrow(()->new ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
//        if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        postEntity.setTitle(postDto.getTitle());
        postEntity.setDescription(postDto.getDescription());
        postEntity.setContent(postDto.getContent());
        PostEntity updatedUserDetails = postRepository.save(postEntity);
        BeanUtils.copyProperties(updatedUserDetails, returnValue);
        return returnValue;

    }

    @Override
    public void deletePost(String postId) {
        PostEntity postEntity = postRepository.findByPostId(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post with id: " + postId + " not found"));
//        if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        postRepository.delete(postEntity);
    }


}
