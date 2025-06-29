package com.example.minioproject.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionAdvice {
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleBadJson(JsonProcessingException ex){
        return ResponseEntity.badRequest().body("Malformed JSON:"+ex.getOriginalMessage());
    }
}
