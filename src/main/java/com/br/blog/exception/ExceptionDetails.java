package com.br.blog.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionDetails {
    private String message;
    private String title;
    protected int status;
    protected String details;
    protected String developerMessage;
    protected LocalDateTime timestamp;

}