package com.br.blog.util;

import com.br.blog.model.Blog;

import java.time.LocalDateTime;

public class BlogCreate {

    public static Blog createBlogUser1(){
        return Blog.builder()
                .id(1L)
                .title("Today")
                .content("Today I'm guessing very hot, because the temperature is 38 Celsius")
                .author(UserCreate.createUser1())
                .created_at(LocalDateTime.now())
                .build();
    }

    public static Blog createBlogUser2(){
        return Blog.builder()
                .id(2L)
                .title("Java")
                .content("Java is very good , because it to give many option to management the code")
                .author(UserCreate.createUser2())
                .created_at(LocalDateTime.now())
                .build();
    }
}
