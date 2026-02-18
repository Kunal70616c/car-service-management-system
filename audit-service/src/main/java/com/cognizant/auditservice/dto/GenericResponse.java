package com.cognizant.auditservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {

    private String message;
    private T data;
    private LocalDateTime timestamp;

    public GenericResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public GenericResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}