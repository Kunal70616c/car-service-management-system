package com.cognizant.auditservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuditRequest {

    @NotNull(message = "Car service id required")
    private Long carServiceId;

    @NotBlank(message = "Action required")
    private String action;

    @NotBlank(message = "PerformedBy required")
    private String performedBy;

    private String details;
}