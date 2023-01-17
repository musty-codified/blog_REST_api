package com.decagon.blogRESTapi.repositories;

import com.decagon.blogRESTapi.entities.CommentEntity;
import com.decagon.blogRESTapi.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByPostDetails(PostEntity postEntity);
    CommentEntity findByPostDetails(PostEntity postEntity);

}
