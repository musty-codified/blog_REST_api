package com.decagon.blogRESTapi.models.request;


import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class UserDetailsRequestModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
