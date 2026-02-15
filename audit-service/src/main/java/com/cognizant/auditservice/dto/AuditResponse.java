package com.cognizant.auditservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditResponse {
    private Long id;
    private Long carServiceId;
    private String action;
    private String performedBy;
    private String details;
    private LocalDateTime timestamp;
}