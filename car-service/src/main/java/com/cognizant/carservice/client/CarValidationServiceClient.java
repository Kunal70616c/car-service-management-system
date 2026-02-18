package com.cognizant.carservice.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CarValidationServiceClient {

    private final RestClient restClient;

    @Value("${car.validation.url}")
    private String validationUrl;

    public boolean isCarValid(String carRegistrationNumber) {

        CarValidationRequest request = new CarValidationRequest();
        request.setCarRegistrationNumber(carRegistrationNumber);

        Map response =
                restClient.post()
                        .uri(validationUrl)
                        .body(request)
                        .retrieve()
                        .body(Map.class);

        if (response == null) {
            throw new RuntimeException("Car validation service failed");
        }

        Map data = (Map) response.get("data");

        if (data == null) {
            throw new RuntimeException("Validation response missing");
        }

        return (Boolean) data.get("isValid");
    }

    // ===== REQUEST DTO =====
    @Data
    static class CarValidationRequest {
        private String carRegistrationNumber;
    }
}