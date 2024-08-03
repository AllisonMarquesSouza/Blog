package com.br.blog.dtos;
import jakarta.validation.constraints.NotBlank;

public record RegisterDto (@NotBlank String username, @NotBlank String password, @NotBlank String email){

}