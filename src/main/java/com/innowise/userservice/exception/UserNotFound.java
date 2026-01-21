package com.innowise.userservice.exception;

public class UserNotFound extends RuntimeException {
    private static final String message = "User not found";
    public UserNotFound() {
        super(message);

    }
}
