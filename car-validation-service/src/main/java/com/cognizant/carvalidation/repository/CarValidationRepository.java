package com.cognizant.carvalidation.repository;

import com.cognizant.carvalidation.model.CarValidationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarValidationRepository extends JpaRepository<CarValidationLog, Long> {
}