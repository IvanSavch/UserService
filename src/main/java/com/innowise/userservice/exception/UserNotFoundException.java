package com.innowise.userservice.exception;

public class UserNotFoundException extends RuntimeException {
    private static final String message = "User not found";
    public UserNotFoundException() {
        super(message);

    }
}
