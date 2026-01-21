package com.innowise.userservice.exception;

public class DuplicateEmailException extends RuntimeException {
    private static final String message = "This email already exist";
    public DuplicateEmailException() {
        super(message);
    }
}
