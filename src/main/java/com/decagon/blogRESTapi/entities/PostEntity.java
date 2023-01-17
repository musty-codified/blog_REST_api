package com.decagon.blogRESTapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;


@Entity(name = "posts")
@Getter
@Setter
public class PostEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String postId;

    @Column(nullable = false, length =50)
    private String title;

    @Column(nullable = false, length =500)
    private String description;

    @Column(nullable = false, length =120)
    private String content;

    @OneToMany(mappedBy ="postDetails", cascade = CascadeType.ALL)
    private Set<CommentEntity> comments;

}
