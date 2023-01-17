package com.decagon.blogRESTapi.controllers;


import com.decagon.blogRESTapi.dto.LoginDto;
import com.decagon.blogRESTapi.dto.PostDto;
import com.decagon.blogRESTapi.dto.UserDto;
import com.decagon.blogRESTapi.exceptions.BlogAPIException;
import com.decagon.blogRESTapi.models.request.LoginRequestModel;
import com.decagon.blogRESTapi.models.request.UserDetailsRequestModel;
import com.decagon.blogRESTapi.models.response.ErrorMessages;
import com.decagon.blogRESTapi.models.response.LoginRest;
import com.decagon.blogRESTapi.models.response.PostRest;
import com.decagon.blogRESTapi.models.response.UserRest;
import com.decagon.blogRESTapi.services.AuthService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ModelMapper modelMapper;

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<String> login(@RequestBody LoginRequestModel loginDetails){
        LoginDto loginDto = modelMapper.map(loginDetails, LoginDto.class);

        String loggedIn = authService.login(loginDto);

        return ResponseEntity.ok(loggedIn);
    }


    @PostMapping(value = {"/signup", "/register"})
    public UserRest signup(@RequestBody UserDetailsRequestModel userDetails){
        UserRest returnValue = new UserRest();
        if(userDetails.getFirstName() == null)
            throw new BlogAPIException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = authService.signup(userDto);
        returnValue = modelMapper.map(createdUser, UserRest.class);
        return returnValue;
    }

}
