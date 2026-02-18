package com.cognizant.carvalidation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarValidationRequest {

    @NotBlank(message = "Car registration number required")
    private String carRegistrationNumber;
}