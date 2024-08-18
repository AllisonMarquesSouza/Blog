package com.br.blog.dtos.blog;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class BlogDtoPost {

    @NotBlank(message = "The title can't be empty or null")
    private String title;

    @NotBlank(message = "The content can't be empty or null")
    private String content;

}
