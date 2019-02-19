package com.concurrent.programming.customexceptions;

public class IllegalDimensionException extends Exception {
    String message;

    public IllegalDimensionException(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
