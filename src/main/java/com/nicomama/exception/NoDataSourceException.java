package com.nicomama.exception;

public class NoDataSourceException extends RuntimeException {
    public NoDataSourceException() {
    }

    public NoDataSourceException(String message) {
        super(message);
    }

    public NoDataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDataSourceException(Throwable cause) {
        super(cause);
    }

    public NoDataSourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
