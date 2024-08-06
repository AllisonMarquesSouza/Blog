package com.br.blog.controller;

import com.br.blog.dtos.authentication.AuthenticationDto;
import com.br.blog.dtos.authentication.RegisterDto;
import com.br.blog.service.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDto authenticationDto){
        return authorizationService.login(authenticationDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody @Valid RegisterDto registerDto){
        return authorizationService.register(registerDto);
    }

}
