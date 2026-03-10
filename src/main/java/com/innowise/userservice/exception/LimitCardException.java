package com.innowise.userservice.exception;

import java.io.Serial;

public class LimitCardException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User can't have more than 5 cards";
    @Serial
    private static final long serialVersionUID = 4275554864088653449L;

    public LimitCardException() {
        super(DEFAULT_MESSAGE);
    }

    public LimitCardException(String message) {
        super(message);
    }

    public LimitCardException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitCardException(Throwable cause) {
        super(cause);
    }
}
