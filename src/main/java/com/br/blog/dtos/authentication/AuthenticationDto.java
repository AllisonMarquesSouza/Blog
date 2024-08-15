package com.br.blog.dtos.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthenticationDto{
    @NotBlank(message = "The username can't be empty or null")
    private String username;

    @NotBlank(message = "The password can't be empty or null")
    private String password;
}
