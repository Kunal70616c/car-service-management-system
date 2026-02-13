package com.cognizant.carservice.model;

import com.cognizant.carservice.enums.ServiceStatus;
import com.cognizant.carservice.enums.ServiceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "car_services")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @Column(name = "car_registration_number", nullable = false, unique = true, length = 20)
    @NotBlank(message = "Car registration number is required")
    private String carRegistrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    @NotNull(message = "Service type is required")
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_status", nullable = false)
    @NotNull(message = "Service status is required")
    private ServiceStatus serviceStatus;

    @Column(name = "service_date", nullable = false)
    @NotNull(message = "Service date is required")
    private LocalDate serviceDate;

    @Column(name = "notes")
    @Length(max = 250, message = "Notes must be at most 250 characters long")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // PrePersist and PreUpdate annotations are used to set the createdAt and updatedAt fields
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // PreUpdate annotation is used to set the updatedAt field
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
