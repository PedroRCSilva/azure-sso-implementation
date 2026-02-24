package com.pedro.azure_sso_implementation.domain.exceptions;


public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
