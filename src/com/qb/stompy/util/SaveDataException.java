package com.qb.stompy.util;

public class SaveDataException extends RuntimeException {
    public SaveDataException() {
    }

    public SaveDataException(String message) {
        super(message);
    }

    public SaveDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveDataException(Throwable cause) {
        super(cause);
    }

    public SaveDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
