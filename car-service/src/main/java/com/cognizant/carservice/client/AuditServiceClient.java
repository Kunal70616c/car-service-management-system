package com.cognizant.carservice.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuditServiceClient {

    private final RestClient restClient;

    @Value("${audit.service.url}")
    private String auditUrl;

    public void sendAudit(Long carServiceId, String action, String performedBy, String details) {

        AuditRequest request = new AuditRequest();
        request.setCarServiceId(carServiceId);
        request.setAction(action);
        request.setPerformedBy(performedBy);
        request.setDetails(details);

        restClient.post()
                .uri(auditUrl)
                .body(request)
                .retrieve()
                .body(Map.class); // no need response
    }

    @Data
    static class AuditRequest {
        private Long carServiceId;
        private String action;
        private String performedBy;
        private String details;
    }
}