package com.concurrent.programming.customexceptions;

public class ConfigException extends Exception {
    String message;

    public ConfigException(String s) {
        this.message = s;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
