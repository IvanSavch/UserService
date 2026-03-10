package com.innowise.userservice.exception;

import java.io.Serial;

public class DuplicateEmailException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "This email already exist";
    @Serial
    private static final long serialVersionUID = -6565421091545394418L;

    public DuplicateEmailException(String message) {
        super(message);
    }

    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateEmailException(Throwable cause) {
        super(cause);
    }
    public DuplicateEmailException() {
        super(DEFAULT_MESSAGE);
    }
}
