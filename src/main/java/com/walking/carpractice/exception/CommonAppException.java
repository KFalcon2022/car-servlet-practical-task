package com.walking.carpractice.exception;

public class CommonAppException extends RuntimeException {
    public CommonAppException(String message) {
        super(message);
    }

    public CommonAppException(Throwable cause) {
        super(cause);
    }

    public CommonAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
