package com.jmorla.tstack.exception;

public class TstackException extends RuntimeException {

    public TstackException(String message) {
        super(message);
    }

    public TstackException(String message, Throwable cause) {
        super(message, cause);
    }

    public TstackException(Throwable cause) {
        super(cause);
    }
}
