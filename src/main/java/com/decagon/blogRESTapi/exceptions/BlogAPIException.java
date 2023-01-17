package com.decagon.blogRESTapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BlogAPIException extends RuntimeException{

    public BlogAPIException(String message) {
        super(message);
    }
}
