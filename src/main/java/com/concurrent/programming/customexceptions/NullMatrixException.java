package com.concurrent.programming.customexceptions;

public class NullMatrixException extends Exception {
    String message;

    public NullMatrixException(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
