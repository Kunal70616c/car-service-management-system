package com.cognizant.carservice.service;

import com.cognizant.carservice.client.AuditServiceClient;
import com.cognizant.carservice.client.CarValidationServiceClient;
import com.cognizant.carservice.client.UserServiceClient;
import com.cognizant.carservice.exception.CustomerNotFoundException;
import com.cognizant.carservice.exception.DuplicateResourceException;
import com.cognizant.carservice.exception.InvalidCarException;
import com.cognizant.carservice.exception.ResourceNotFoundException;
import com.cognizant.carservice.model.CarService;
import com.cognizant.carservice.repository.CarServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceServiceImpl implements CarServiceService {

    private final CarServiceRepository repository;
    private final UserServiceClient userServiceClient;
    private final CarValidationServiceClient carValidationClient;
    private final AuditServiceClient auditClient;

    // ===== helper to get logged in username from JWT =====
    private String getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return "SYSTEM";
        return auth.getName(); // username from keycloak token
    }

    @Override
    public CarService addCarService(CarService carService) {

        if (carService.getServiceDate() == null) {
            throw new RuntimeException("Service date is required");
        }

        if (carService.getServiceDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Service date cannot be in future");
        }

        // validate user exists
        if (!userServiceClient.userExists(carService.getCustomerId())) {
            throw new CustomerNotFoundException("Customer does not exist");
        }

        // duplicate car check
        if (repository.existsByCarRegistrationNumber(carService.getCarRegistrationNumber())) {
            throw new DuplicateResourceException("Car already exists");
        }

        // validate car number via car-validation-service
        boolean validCar = carValidationClient.isCarValid(carService.getCarRegistrationNumber());

        if (!validCar) {
            throw new InvalidCarException("Invalid car registration number");
        }

        CarService saved = repository.save(carService);

        // ===== SEND AUDIT =====
        auditClient.sendAudit(
                saved.getId(),
                "CAR_CREATED",
                getLoggedInUser(),
                "Car service created"
        );

        return saved;
    }

    @Override
    public List<CarService> getAllCarServices() {
        return repository.findAll();
    }

    @Override
    public CarService getCarServiceById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car service not found with id: " + id));
    }

    @Override
    public CarService updateCarService(Long id, CarService carService) {

        CarService existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car service not found with id: " + id));

        if (carService.getServiceDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Service date cannot be in future");
        }

        existing.setServiceType(carService.getServiceType());
        existing.setServiceStatus(carService.getServiceStatus());
        existing.setServiceDate(carService.getServiceDate());
        existing.setNotes(carService.getNotes());

        CarService updated = repository.save(existing);

        // ===== AUDIT =====
        auditClient.sendAudit(
                updated.getId(),
                "CAR_UPDATED",
                getLoggedInUser(),
                "Car service updated"
        );

        return updated;
    }

    @Override
    public void deleteCarService(Long id) {

        CarService existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car service not found with id: " + id));

        repository.delete(existing);

        // ===== AUDIT =====
        auditClient.sendAudit(
                id,
                "CAR_DELETED",
                getLoggedInUser(),
                "Car service deleted"
        );
    }
}