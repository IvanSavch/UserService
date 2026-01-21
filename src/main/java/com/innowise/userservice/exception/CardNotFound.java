package com.innowise.userservice.exception;

public class CardNotFound extends RuntimeException {
    private static final String message = "Card not found";
    public CardNotFound() {
        super(message);
    }
}
