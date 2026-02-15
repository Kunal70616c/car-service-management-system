package com.cognizant.auditservice.service;

import com.cognizant.auditservice.dto.AuditRequest;
import com.cognizant.auditservice.dto.AuditResponse;
import com.cognizant.auditservice.exception.ResourceNotFoundException;
import com.cognizant.auditservice.model.AuditLog;
import com.cognizant.auditservice.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository repository;

    @Override
    public AuditResponse logAction(AuditRequest request) {

        if (request.getCarServiceId() == null) {
            throw new RuntimeException("Car service ID cannot be null");
        }

        if (request.getAction() == null || request.getAction().isBlank()) {
            throw new RuntimeException("Action is required");
        }

        if (request.getPerformedBy() == null || request.getPerformedBy().isBlank()) {
            throw new RuntimeException("PerformedBy is required");
        }

        AuditLog log = AuditLog.builder()
                .carServiceId(request.getCarServiceId())
                .action(request.getAction())
                .performedBy(request.getPerformedBy())
                .details(request.getDetails())
                .build();

        AuditLog saved = repository.save(log);

        return AuditResponse.builder()
                .id(saved.getId())
                .carServiceId(saved.getCarServiceId())
                .action(saved.getAction())
                .performedBy(saved.getPerformedBy())
                .details(saved.getDetails())
                .timestamp(saved.getTimestamp())
                .build();
    }

    @Override
    public List<AuditResponse> getAllLogs() {

        List<AuditLog> logs = repository.findAll();

        if (logs.isEmpty()) {
            throw new ResourceNotFoundException("No audit logs found");
        }

        return logs.stream()
                .map(log -> AuditResponse.builder()
                        .id(log.getId())
                        .carServiceId(log.getCarServiceId())
                        .action(log.getAction())
                        .performedBy(log.getPerformedBy())
                        .details(log.getDetails())
                        .timestamp(log.getTimestamp())
                        .build())
                .toList();
    }
}