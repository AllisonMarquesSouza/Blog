package com.br.blog.dtos.blog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BlogDtoPut {
    @NotNull(message = "The id can't be null")
    private Long id;

    @NotBlank(message = "The title can't be empty or null")
    private String title;

    @NotBlank(message = "The content can't be empty or null")
    private String content;

}
