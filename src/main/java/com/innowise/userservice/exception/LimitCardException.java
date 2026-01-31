package com.innowise.userservice.exception;

public class LimitCardException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User can't have more than 5 cards";
    public LimitCardException() {
        super(DEFAULT_MESSAGE);
    }
}
