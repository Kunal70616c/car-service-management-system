package com.cognizant.carservice.service;

import com.cognizant.carservice.model.CarService;

import java.util.List;

public interface CarServiceService {

    CarService addCarService(CarService carService);

    List<CarService> getAllCarServices();

    CarService getCarServiceById(Long id);

    CarService updateCarService(Long id, CarService carService);

    void deleteCarService(Long id);
}