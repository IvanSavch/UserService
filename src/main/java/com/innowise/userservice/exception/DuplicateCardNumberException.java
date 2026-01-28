package com.innowise.userservice.exception;

public class DuplicateCardNumberException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "This card already exist";
    public DuplicateCardNumberException() {
        super(DEFAULT_MESSAGE);
    }
}
