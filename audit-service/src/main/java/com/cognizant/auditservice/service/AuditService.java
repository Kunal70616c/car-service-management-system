package com.cognizant.auditservice.service;

import com.cognizant.auditservice.dto.AuditRequest;
import com.cognizant.auditservice.dto.AuditResponse;

import java.util.List;

public interface AuditService {

    AuditResponse logAction(AuditRequest request);

    List<AuditResponse> getAllLogs();
}