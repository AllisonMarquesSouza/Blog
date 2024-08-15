package com.br.blog.util;

import com.br.blog.enums.UserRole;
import com.br.blog.model.User;

public class UserCreate {

    public static User createUser1(){
        return User.builder()
                .id(1L)
                .username("allison")
                .password("123456")
                .email("allison@email.com")
                .role(UserRole.USER)
                .build();
    }

    public static User createUser2(){
        return User.builder()
                .id(2L)
                .username("thefool")
                .password("654321")
                .email("thefool@email.com")
                .role(UserRole.USER)
                .build();
    }
}
