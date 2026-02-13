package com.cognizant.carservice.dto;

import com.cognizant.carservice.enums.ServiceStatus;
import com.cognizant.carservice.enums.ServiceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarServiceDTO {

    @Schema(hidden = true)
    private Long id;
    private Long customerId;
    private String carRegistrationNumber;
    private ServiceType serviceType;
    private ServiceStatus serviceStatus;
    private LocalDate serviceDate;
    private String notes;

    @Schema(hidden = true)
    private LocalDateTime createdAt;
    @Schema(hidden = true)
    private LocalDateTime updatedAt;

}
