package com.innowise.userservice.exception;

public class CardNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Card not found";
    public CardNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
