package com.br.blog.service;

import com.br.blog.repository.UserRepository;
import com.br.blog.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {
    @Mock
    private ApplicationContext context;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;


    @BeforeEach
    void setUp() {

    }
    @Test
    void login() {

    }

    @Test
    void changePassword() {
    }

    @Test
    void register() {
    }

    @Test
    void getCurrentUser() {
    }
}