package com.br.blog.dtos.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangePasswordDto{
    @NotBlank(message = "The username can't be empty or null")
    private String username;

    @NotBlank(message = "The oldPassword can't be empty or null")
    private String oldPassword;

    @NotBlank(message = "The newPassword can't be empty or null")
    private String newPassword;
}
