package com.cognizant.carservice.exception;

import com.cognizant.carservice.dto.GenericResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testNotFound() {
        ResponseEntity<GenericResponse<?>> res =
                handler.handleNotFound(new ResourceNotFoundException("Not found"));
        assertEquals(404, res.getStatusCode().value());
    }

    @Test
    void testDuplicate() {
        ResponseEntity<GenericResponse<?>> res =
                handler.handleDuplicate(new DuplicateResourceException("Duplicate"));
        assertEquals(400, res.getStatusCode().value());
    }

    @Test
    void testAccessDenied() {
        ResponseEntity<GenericResponse<String>> res =
                handler.handleAccessDeniedException(new AccessDeniedException("Denied"));
        assertEquals(403, res.getStatusCode().value());
    }

    @Test
    void testGeneric() {
        ResponseEntity<GenericResponse<?>> res =
                handler.handleGeneric(new RuntimeException("Error"));
        assertEquals(500, res.getStatusCode().value());
    }
}