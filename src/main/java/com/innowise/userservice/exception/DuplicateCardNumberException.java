package com.innowise.userservice.exception;

import java.io.Serial;

public class DuplicateCardNumberException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "This card already exist";
    @Serial
    private static final long serialVersionUID = 3538581320205463945L;

    public DuplicateCardNumberException(String message) {
        super(message);
    }

    public DuplicateCardNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateCardNumberException(Throwable cause) {
        super(cause);
    }

    public DuplicateCardNumberException() {
        super(DEFAULT_MESSAGE);
    }
}
