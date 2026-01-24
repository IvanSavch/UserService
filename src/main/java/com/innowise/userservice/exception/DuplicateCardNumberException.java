package com.innowise.userservice.exception;

public class DuplicateCardNumberException extends RuntimeException {
    private static final String message = "This card already exist";
    public DuplicateCardNumberException() {
        super(message);
    }
}
