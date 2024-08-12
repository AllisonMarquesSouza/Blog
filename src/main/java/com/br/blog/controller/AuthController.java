package com.br.blog.controller;
import com.br.blog.dtos.authentication.AuthenticationDto;
import com.br.blog.dtos.authentication.ChangePasswordDto;
import com.br.blog.dtos.authentication.LoginResponseDto;
import com.br.blog.dtos.authentication.RegisterDto;
import com.br.blog.model.User;
import com.br.blog.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthenticationDto authenticationDto){
        return ResponseEntity.ok(authenticationService.login(authenticationDto));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register (@RequestBody @Valid RegisterDto registerDto){
        return new ResponseEntity<>(authenticationService.register(registerDto), HttpStatus.CREATED);
    }
    @PutMapping("/changePassword")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordDto data){
        authenticationService.changePassword(data);
        return ResponseEntity.noContent().build();
    }


}
