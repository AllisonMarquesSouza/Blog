package com.br.blog.dtos.blog;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class BlogDtoPost {

    @NotBlank(message = "Don't can be empty or null")
    private String title;

    @NotBlank(message = "Don't can be empty or null")
    private String content;

}
