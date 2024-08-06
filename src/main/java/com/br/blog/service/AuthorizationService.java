package com.br.blog.service;

import com.br.blog.dtos.authentication.AuthenticationDto;
import com.br.blog.dtos.authentication.LoginResponseDto;
import com.br.blog.dtos.authentication.RegisterDto;
import com.br.blog.enums.UserRole;
import com.br.blog.model.User;
import com.br.blog.repository.UserRepository;
import com.br.blog.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;
    private  final TokenService tokenService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDto data){
        AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);

        if(auth.isAuthenticated()){
            var token = tokenService.generateToken((User) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDto(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }


    public ResponseEntity<?> register (@RequestBody RegisterDto registerDto){
        String message = "Error check email or username";
        if (this.userRepository.findByUsername(registerDto.getUsername()) != null || this.userRepository.findByEmail(registerDto.getEmail()) != null){
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.getPassword());

        User newUser = new User(registerDto.getUsername(), encryptedPassword, registerDto.getEmail(), UserRole.USER);
        this.userRepository.save(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails;
        }
        throw new UsernameNotFoundException("User not found");
    }
}
