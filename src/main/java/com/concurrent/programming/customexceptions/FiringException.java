package com.concurrent.programming.customexceptions;

public class FiringException extends Exception {
    String message;

    public FiringException(String s) {
        this.message = s;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
