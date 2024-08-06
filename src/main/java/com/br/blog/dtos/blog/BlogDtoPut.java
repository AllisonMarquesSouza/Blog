package com.br.blog.dtos.blog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BlogDtoPut {
    @NotNull(message = "Don't can be null")
    private Long id;

    @NotBlank(message = "Don't can be empty or null")
    private String title;

    @NotBlank(message = "Don't can be empty or null")
    private String content;

}
