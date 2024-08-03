package com.br.blog.controller;

import com.br.blog.dtos.AuthenticationDto;
import com.br.blog.dtos.RegisterDto;
import com.br.blog.service.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDto authenticationDto){
        return authorizationService.login(authenticationDto);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegisterDto registerDto){
        return authorizationService.register(registerDto);
    }
}
