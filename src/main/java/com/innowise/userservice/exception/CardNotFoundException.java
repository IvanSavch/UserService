package com.innowise.userservice.exception;

import java.io.Serial;

public class CardNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Card not found";
    @Serial
    private static final long serialVersionUID = -7012190114963218894L;

    public CardNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public CardNotFoundException(String message) {
        super(message);
    }

    public CardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardNotFoundException(Throwable cause) {
        super(cause);
    }
}
