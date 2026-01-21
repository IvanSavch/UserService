package com.innowise.userservice.exception;

public class LimitCardException extends RuntimeException {
    private static final String message = "User can't have more than 5 cards";
    public LimitCardException() {
        super(message);
    }
}
