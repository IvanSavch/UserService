package com.innowise.userservice.exception;

public class CardNotFoundException extends RuntimeException {
    private static final String message = "Card not found";
    public CardNotFoundException() {
        super(message);
    }
}
