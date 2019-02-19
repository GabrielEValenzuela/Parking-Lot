package com.concurrent.programming.customexceptions;

public class NoPlaceInvariants extends Exception {
    String message;

    public NoPlaceInvariants(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
