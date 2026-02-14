package com.cognizant.userservice.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GenericResponse<T> {

    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    public GenericResponse(String message) {
        this.message = message;
        this.data = null;
        this.timestamp = LocalDateTime.now();
    }

    public GenericResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}