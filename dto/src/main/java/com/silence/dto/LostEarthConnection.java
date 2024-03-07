package com.silence.dto;

public class LostEarthConnection extends Throwable{

    private final String message;

    public LostEarthConnection(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LostEarthConnection{" +
                "message='" + message + '\'' +
                '}';
    }
}
