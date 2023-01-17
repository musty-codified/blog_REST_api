package com.decagon.blogRESTapi.services.impl;

import com.decagon.blogRESTapi.entities.UserEntity;
import com.decagon.blogRESTapi.models.response.ErrorMessage;
import com.decagon.blogRESTapi.models.response.ErrorMessages;
import com.decagon.blogRESTapi.repositories.RoleRepository;
import com.decagon.blogRESTapi.repositories.UserRepository;
import com.decagon.blogRESTapi.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository repository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity =  userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

//        if(userEntity == null) throw new UsernameNotFoundException(email);

        Set<GrantedAuthority> authorities = userEntity
                .getRoles()
                .stream()
                .map((role)->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        return  new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),authorities);

//        return new User(userEntity.getEmail(),
//                userEntity.getEncryptedPassword(),
//                userEntity.getEmailVerificationStatus(),
//                true,
//                true,
//                true,
//                new ArrayList<>());
//      return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>()) ;
    }
    }
