package com.br.blog.controller;
import com.br.blog.dtos.authentication.AuthenticationDto;
import com.br.blog.dtos.authentication.LoginResponseDto;
import com.br.blog.dtos.authentication.RegisterDto;
import com.br.blog.model.User;
import com.br.blog.service.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthenticationDto authenticationDto){
        return ResponseEntity.ok(authorizationService.login(authenticationDto));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register (@RequestBody @Valid RegisterDto registerDto){
        return new ResponseEntity<>(authorizationService.register(registerDto), HttpStatus.CREATED);
    }


}
