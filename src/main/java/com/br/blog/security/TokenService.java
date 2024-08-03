package com.br.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.br.blog.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    //key to JWT token , set in environment variable
    @Value("${JWT_SECRET}")
    private String secret;

    public String generateToken(Usuario userModel){

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

    //Validating token
    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            //Here , require token and verify if is correct
            return JWT.require(algorithm)
                    .withIssuer("auth")
                    .build()
                    .verify(token)
                    .getSubject();//In this case , is the username. Because i have
        }

        catch (JWTVerificationException exception) {
            throw new JWTVerificationException("ERROR WHILE VALIDATING TOKEN", exception);
        }
    }

    //Time that was defined to expiration token
    private Instant getExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
