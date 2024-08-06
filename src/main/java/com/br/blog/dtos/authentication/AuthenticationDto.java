package com.br.blog.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthenticationDto{
    @NotBlank(message = "Don't can be empty or null")
    private String username;

    @NotBlank(message = "Don't can be empty or null")
    private String password;
}
