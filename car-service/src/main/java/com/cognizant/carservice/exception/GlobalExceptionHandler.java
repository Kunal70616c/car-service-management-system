package com.cognizant.carservice.exception;

import com.cognizant.carservice.dto.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse<?>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(new GenericResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<GenericResponse<?>> handleDuplicate(DuplicateResourceException ex) {
        return ResponseEntity.status(400)
                .body(new GenericResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        return ResponseEntity.status(400)
                .body(new GenericResponse<>("Validation failed", errors));
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GenericResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {

        return ResponseEntity
                .status(403)
                .body(new GenericResponse<>("Access Denied: You are not authorized to perform this action"));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<GenericResponse<?>> handleCustomerNotFound(CustomerNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(new GenericResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<?>> handleGeneric(Exception ex) {
        return ResponseEntity.status(500)
                .body(new GenericResponse<>(ex.getMessage()));
    }
}