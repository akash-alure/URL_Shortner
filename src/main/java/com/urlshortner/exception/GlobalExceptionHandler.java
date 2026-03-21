package com.urlshortner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,Object> handleUrlNotFound(UrlNotFoundException ex){
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "error", ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,Object> handleGeneric(Exception ex){
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "error","Internal Server Error"
        );
    }

    @ExceptionHandler(DomainBlacklistedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Map<String,Object> handleDomainBlacklisted(DomainBlacklistedException ex){
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "error",ex.getMessage()
        );
    }
}
