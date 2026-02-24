package com.pedro.azure_sso_implementation.application.handler;

import com.pedro.azure_sso_implementation.domain.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> returnResponseUnathorized(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
