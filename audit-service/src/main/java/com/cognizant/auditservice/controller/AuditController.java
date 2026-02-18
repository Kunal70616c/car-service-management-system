package com.cognizant.auditservice.controller;

import com.cognizant.auditservice.dto.AuditRequest;
import com.cognizant.auditservice.dto.AuditResponse;
import com.cognizant.auditservice.dto.GenericResponse;
import com.cognizant.auditservice.service.AuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService service;

    // CREATE AUDIT LOG
    @PostMapping("/log")
    public ResponseEntity<GenericResponse<AuditResponse>> logAction(
            @Valid @RequestBody AuditRequest request) {

        AuditResponse response = service.logAction(request);

        return ResponseEntity.status(201)
                .body(new GenericResponse<>("Audit log created", response));
    }

    // GET ALL LOGS
    @GetMapping("/all")
    public ResponseEntity<GenericResponse<List<AuditResponse>>> getAllLogs() {

        List<AuditResponse> logs = service.getAllLogs();

        return ResponseEntity.ok(
                new GenericResponse<>("Audit logs fetched", logs)
        );
    }
}