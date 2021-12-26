package com.example.exception;

public class AccountError {
    private int code;
    private String message;

    public AccountError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}