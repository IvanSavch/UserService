package com.innowise.userservice.exception;

public class DuplicateEmailException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "This email already exist";
    public DuplicateEmailException() {
        super(DEFAULT_MESSAGE);
    }
}
