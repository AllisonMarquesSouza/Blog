package com.br.blog.exception;

import com.br.blog.exception.personalizedExceptions.BadRequestException;
import com.br.blog.exception.personalizedExceptions.ResourceAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

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
                        .details(entityNotFound.getCause() != null ? entityNotFound.getCause().toString() : "No details available")
                        .developerMessage(entityNotFound.getClass().getName())
                        .build(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArg) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .message("Error, invalid Argument")
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("MethodArgumentNotValidException")
                        .details(methodArg.getBindingResult().getFieldErrors().toString())
                        .developerMessage(methodArg.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ExceptionDetails> handleResourceAlreadyExistsException(ResourceAlreadyExistsException resource) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .message(resource.getMessage())
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.CONFLICT.value())
                        .title("ResourceAlreadyExistsException")
                        .details(resource.getCause() != null ? resource.getCause().toString() : "No details available")
                        .developerMessage(resource.getClass().getName())
                        .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDetails> handleBadRequestException(BadRequestException badRequest) {
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .message(badRequest.getMessage())
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("BadRequestException")
                        .details(badRequest.getCause() != null ? badRequest.getCause().toString() : "No details available")
                        .developerMessage(badRequest.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST);
    }
}