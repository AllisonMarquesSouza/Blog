package com.br.blog.dtos.authentication;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterDto {
    @NotBlank(message = "The username can't be empty or null")
    private String username;

    @NotBlank(message = "The password can't be empty or null")
    private String password;

    @NotBlank(message = "The email can't be empty or null")
    private String email;
}

