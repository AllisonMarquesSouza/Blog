package com.br.blog.security;

import com.br.blog.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //getting the token ...
        var token = this.recoverToken(request);

        //checking token if not null
        if (token != null) {
            var username = tokenService.validateToken(token);
            UserDetails user = userRepository.findByUsername(username);

            //Continue to checking and getting authorities now
            if (user != null) {
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                //Setting the authentication with context of the application
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        //It's mean that can to go next filter
        filterChain.doFilter(request, response);
    }

    //Getting token
    private String recoverToken(HttpServletRequest request){

        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
