package com.cognizant.carvalidation.service;

import com.cognizant.carvalidation.dto.CarValidationRequest;
import com.cognizant.carvalidation.dto.CarValidationResponse;

import java.util.List;

public interface CarValidationService {

    CarValidationResponse validateCar(CarValidationRequest request);

    List<CarValidationResponse> getAllLogs();
}