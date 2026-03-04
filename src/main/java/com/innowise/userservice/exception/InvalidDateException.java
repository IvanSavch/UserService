package com.innowise.userservice.exception;

import java.io.Serial;

public class InvalidDateException extends RuntimeException {
    private static final String DEFAULT_MESSAGE ="Invalid date ";
    @Serial
    private static final long serialVersionUID = 4971845899976765224L;

    public InvalidDateException(String message) {
        super(message);
    }

    public InvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDateException(Throwable cause) {
        super(cause);
    }

    public InvalidDateException() {
        super(DEFAULT_MESSAGE);
    }
}
