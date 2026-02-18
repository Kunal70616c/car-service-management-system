package com.cognizant.carvalidation.service.impl;

import com.cognizant.carvalidation.dto.CarValidationRequest;
import com.cognizant.carvalidation.dto.CarValidationResponse;
import com.cognizant.carvalidation.model.CarValidationLog;
import com.cognizant.carvalidation.repository.CarValidationRepository;
import com.cognizant.carvalidation.service.CarValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarValidationServiceImpl implements CarValidationService {

    private final CarValidationRepository repository;

    // MAIN VALIDATION LOGIC
    @Override
    public CarValidationResponse validateCar(CarValidationRequest request) {

        String reg = request.getCarRegistrationNumber();

        // simple regex validation (Indian car format basic)
        boolean isValid = reg.matches("^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$");

        CarValidationLog log = CarValidationLog.builder()
                .carRegistrationNumber(reg)
                .isValid(isValid)
                .build();

        repository.save(log);

        return CarValidationResponse.builder()
                .carRegistrationNumber(reg)
                .isValid(isValid)
                .validationTimestamp(log.getValidationTimestamp())
                .build();
    }

    @Override
    public List<CarValidationResponse> getAllLogs() {
        return repository.findAll().stream().map(log ->
                CarValidationResponse.builder()
                        .carRegistrationNumber(log.getCarRegistrationNumber())
                        .isValid(log.getIsValid())
                        .validationTimestamp(log.getValidationTimestamp())
                        .build()
        ).toList();
    }
}