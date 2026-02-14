package com.cognizant.carvalidation.controller;

import com.cognizant.carvalidation.dto.CarValidationRequest;
import com.cognizant.carvalidation.dto.CarValidationResponse;
import com.cognizant.carvalidation.dto.GenericResponse;
import com.cognizant.carvalidation.service.CarValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/car-validation")
@RequiredArgsConstructor
public class CarValidationController {

    private final CarValidationService service;

    // Validate car registration
    @PostMapping("/validate")
    public ResponseEntity<GenericResponse<CarValidationResponse>> validateCar(
            @Valid @RequestBody CarValidationRequest request) {

        CarValidationResponse response = service.validateCar(request);

        return ResponseEntity.ok(
                new GenericResponse<>("Car validation completed", response)
        );
    }

    // Get validation logs
    @GetMapping("/logs")
    public ResponseEntity<GenericResponse<?>> getAllLogs() {
        return ResponseEntity.ok(
                new GenericResponse<>("Validation logs fetched", service.getAllLogs())
        );
    }
}