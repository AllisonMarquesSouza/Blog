package com.br.blog.dtos.blog.blogResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Data
@AllArgsConstructor
public class BlogResponseDto {
    private String author;
    private String title;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
