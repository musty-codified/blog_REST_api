package com.decagon.blogRESTapi.services;

import com.decagon.blogRESTapi.dto.LoginDto;
import com.decagon.blogRESTapi.dto.UserDto;

public interface AuthService {
    String login(LoginDto loginDto);

    UserDto signup(UserDto userDto);
}
