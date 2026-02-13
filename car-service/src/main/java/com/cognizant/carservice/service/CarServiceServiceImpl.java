package com.cognizant.carservice.service;

import com.cognizant.carservice.exception.DuplicateResourceException;
import com.cognizant.carservice.exception.ResourceNotFoundException;
import com.cognizant.carservice.model.CarService;
import com.cognizant.carservice.repository.CarServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceServiceImpl implements CarServiceService {

    private final CarServiceRepository repository;

    @Override
    public CarService addCarService(CarService carService) {

        // ensure new record
        carService.setId(null);

        if (carService.getServiceDate() == null) {
            throw new RuntimeException("Service date is required");
        }

        if (carService.getServiceDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Service date cannot be in future");
        }

        if (repository.existsByCarRegistrationNumber(carService.getCarRegistrationNumber())) {
            throw new DuplicateResourceException("Car with this registration already exists");
        }

        return repository.save(carService);
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

        // future date validation

        if (carService.getServiceDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Service date cannot be in future");
        }

        existing.setServiceType(carService.getServiceType());
        existing.setServiceStatus(carService.getServiceStatus());
        existing.setServiceDate(carService.getServiceDate());
        existing.setNotes(carService.getNotes());

        return repository.save(existing);
    }

    @Override
    public void deleteCarService(Long id) {

        CarService existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car service not found with id: " + id));

        repository.delete(existing);
    }
}