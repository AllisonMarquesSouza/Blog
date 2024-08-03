package com.br.blog.service;

import com.br.blog.dtos.AuthenticationDto;
import com.br.blog.dtos.LoginResponseDto;
import com.br.blog.dtos.RegisterDto;
import com.br.blog.enums.UserRole;
import com.br.blog.model.Usuario;
import com.br.blog.repository.UsuarioRepository;
import com.br.blog.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final ApplicationContext context;
    private final UsuarioRepository userRepository;
    private  final TokenService tokenService;

    //
    private  AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDto data){
        authenticationManager = context.getBean(AuthenticationManager.class);

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        if(auth.isAuthenticated()){
            var token = tokenService.generateToken((Usuario) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDto(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }


    public ResponseEntity<?> register (@RequestBody RegisterDto registerDto){
        String message = "Error check email or username";
        if (this.userRepository.findByUsername(registerDto.username()) != null || this.userRepository.findByEmail(registerDto.email()) != null){
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.password());

        Usuario newUser = new Usuario(registerDto.username(), encryptedPassword, registerDto.email(), UserRole.USER);
        this.userRepository.save(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
