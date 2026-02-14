package com.cognizant.carvalidation.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "car_validation_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarValidationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "car_registration_number", nullable = false, length = 20)
    private String carRegistrationNumber;

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid;

    @Column(name = "validation_timestamp", nullable = false)
    private LocalDateTime validationTimestamp;

    @PrePersist
    public void prePersist() {
        validationTimestamp = LocalDateTime.now();
    }
}