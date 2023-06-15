package com.omnicommerce.golobal.exception;

public enum ErrorCodes {
    E00001("internal error"),
    E00002("saving failure"),
    E00003("user not found"),
    E00004("login failure"),
    E00005("token wrong"),
    E00006("authorization failure");

    private final String message;

    ErrorCodes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
