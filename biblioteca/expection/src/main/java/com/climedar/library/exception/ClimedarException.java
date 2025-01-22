package com.climedar.library.exception;

public class ClimedarException extends RuntimeException {
    private String code;
    public ClimedarException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
