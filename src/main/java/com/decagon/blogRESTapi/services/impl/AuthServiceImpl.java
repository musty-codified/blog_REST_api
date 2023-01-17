package com.decagon.blogRESTapi.services.impl;

import com.decagon.blogRESTapi.dto.LoginDto;
import com.decagon.blogRESTapi.dto.UserDto;
import com.decagon.blogRESTapi.dto.Utils;
import com.decagon.blogRESTapi.entities.RoleEntity;
import com.decagon.blogRESTapi.entities.UserEntity;
import com.decagon.blogRESTapi.exceptions.BlogAPIException;
import com.decagon.blogRESTapi.models.request.LoginRequestModel;
import com.decagon.blogRESTapi.models.response.ErrorMessages;
import com.decagon.blogRESTapi.repositories.RoleRepository;
import com.decagon.blogRESTapi.repositories.UserRepository;
import com.decagon.blogRESTapi.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final Utils utils;
    @Override
    public String login(LoginDto loginDto) {

        LoginRequestModel creds = new LoginRequestModel();
    Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.getEmail(),
                        creds.getPassword(),
                        new ArrayList<>()));

        SecurityContextHolder.getContext().setAuthentication(authentication);


        return "User Logged-in successfully";
    }

    @Override
    public UserDto signup(UserDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail()))
            throw new BlogAPIException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());

        UserEntity userEntity =  modelMapper.map(userDto, UserEntity.class);
        String publicUserId = utils.generateUserId(10);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        userEntity.setRoles(roles);
        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue=  modelMapper.map(storedUserDetails, UserDto.class);
        return returnValue;
    }
}
