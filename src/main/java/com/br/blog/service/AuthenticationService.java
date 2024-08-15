package com.br.blog.service;

import com.br.blog.dtos.authentication.AuthenticationDto;
import com.br.blog.dtos.authentication.ChangePasswordDto;
import com.br.blog.dtos.authentication.LoginResponseDto;
import com.br.blog.dtos.authentication.RegisterDto;
import com.br.blog.enums.UserRole;
import com.br.blog.exception.personalizedExceptions.BadRequestException;
import com.br.blog.exception.personalizedExceptions.ResourceAlreadyExistsException;
import com.br.blog.model.User;
import com.br.blog.repository.UserRepository;
import com.br.blog.security.TokenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService implements UserDetailsService {
    private final ApplicationContext context;
    private final UserRepository userRepository;
    private  final TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public LoginResponseDto login(AuthenticationDto data){
        AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
            var auth = authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());
            return new LoginResponseDto(token);
        } catch (Exception e) {
            throw new BadRequestException("Don't possible to make login , check your username and password");
        }
    }
    public void changePassword(ChangePasswordDto data){
        User userFound = userRepository.findByUsernameToUSER(data.getUsername())
                .orElseThrow(()-> new EntityNotFoundException("Username not found"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean checkingPassword = encoder.matches(data.getOldPassword(), userFound.getPassword());
        if (!checkingPassword){
            throw new BadRequestException("Old password is incorrect , check it");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getNewPassword());
        userFound.setPassword(encryptedPassword);
        userRepository.save(userFound);
    }

    @Transactional
    public User register (RegisterDto registerDto){
        if (this.userRepository.findByUsername(registerDto.getUsername()) != null || this.userRepository.findByEmail(registerDto.getEmail()) != null){
            String message = "Email or username, already exist!";
            throw new ResourceAlreadyExistsException(message);
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.getPassword());
        User newUser = new User(registerDto.getUsername(), encryptedPassword, registerDto.getEmail(), UserRole.USER);
        return this.userRepository.save(newUser);
    }

    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails;
        }
        throw new EntityNotFoundException("User not found");
    }
}
