package com.innowise.userservice.exception;

public class InvalidDateException extends RuntimeException {
    private static final String DEFAULT_MESSAGE ="Invalid date ";
    public InvalidDateException() {
        super(DEFAULT_MESSAGE);
    }
}
