package com.cognizant.carservice.repository;

import com.cognizant.carservice.model.CarService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarServiceRepository extends JpaRepository<CarService, Long> {

    Optional<CarService> findByCarRegistrationNumber(String carRegistrationNumber);

    boolean existsByCarRegistrationNumber(String carRegistrationNumber);
}