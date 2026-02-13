package com.cognizant.carservice.dto;


import com.cognizant.carservice.enums.ServiceStatus;
import com.cognizant.carservice.enums.ServiceType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CarServiceResponse(Long id, Long customerId,
                                 String carRegistrationNumber,
                                 ServiceType serviceType,
                                 ServiceStatus serviceStatus,
                                 LocalDate serviceDate,
                                 String notes,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt) {
}
