package com.innowise.userservice.exception;

public class DuplicateCardNumber extends RuntimeException {
    private static final String message = "This card already exist";
    public DuplicateCardNumber() {
        super(message);
    }
}
