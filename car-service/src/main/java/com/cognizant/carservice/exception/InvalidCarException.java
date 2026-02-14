package com.cognizant.carservice.exception;

public class InvalidCarException extends RuntimeException {
    public InvalidCarException(String message) {
        super(message);
    }
}