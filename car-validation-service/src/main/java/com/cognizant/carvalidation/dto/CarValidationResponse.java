package com.cognizant.carvalidation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarValidationResponse {

    private String carRegistrationNumber;
    private Boolean isValid;
    private LocalDateTime validationTimestamp;
}