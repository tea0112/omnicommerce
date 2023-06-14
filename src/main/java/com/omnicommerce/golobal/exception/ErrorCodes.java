package com.omnicommerce.golobal.exception;

public enum ErrorCodes {
    E00001("internal exception"),
    E00002("saving exception"),
    E00003("user not found exception"),
    E00004("login exception");

    private final String message;

    ErrorCodes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
