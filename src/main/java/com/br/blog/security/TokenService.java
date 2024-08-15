package com.br.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.br.blog.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    //key to JWT token , set in environment variable
    @Value("${JWT_SECRET:your-key-specific}")
    private String secret;

    public String generateToken(User userModel){

        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);

            //Generating token
            String token = JWT.create()
                    .withIssuer("auth")
                    .withSubject(userModel.getUsername())
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);
            return token;


        } catch (JWTCreationException exception) {
            throw new JWTCreationException("ERROR WHILE GENERATING TOKEN", exception);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            //Here , require token and verify if is correct
            return JWT.require(algorithm)
                    .withIssuer("auth")
                    .build()
                    .verify(token)
                    .getSubject();//In this case , is the username. Because I defined it
        }

        catch (JWTVerificationException exception) {
            throw new JWTVerificationException("ERROR WHILE VALIDATING TOKEN", exception);
        }
    }

    private Instant getExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
