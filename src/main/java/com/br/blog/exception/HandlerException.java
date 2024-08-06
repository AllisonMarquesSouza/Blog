package com.br.blog.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class HandlerException {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleEntityNotFoundException(EntityNotFoundException entityNotFound) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .message(entityNotFound.getMessage())
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND.value())
                        .title("Not Found Exception , Check the Documentation")
                        .details("Entity not found")
                        .developerMessage(entityNotFound.getClass().getName())
                        .build(), HttpStatus.NOT_FOUND);

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArg) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .message(methodArg.getMessage())
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("MethodArgumentNotValidException")
                        .details("Arg is not valid")
                        .developerMessage(methodArg.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDetails> handleMethodArgumentNotValidException(AccessDeniedException deniedAccess) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .message(deniedAccess.getMessage())
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.FORBIDDEN.value())
                        .title("AccessDeniedException")
                        .details("You haven't permission to access it")
                        .developerMessage(deniedAccess.getClass().getName())
                        .build(), HttpStatus.FORBIDDEN);
    }
}