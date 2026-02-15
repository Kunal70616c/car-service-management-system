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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceServiceImplTest {

    @Mock
    private CarServiceRepository repository;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private CarValidationServiceClient carValidationClient;

    @Mock
    private AuditServiceClient auditClient;

    @InjectMocks
    private CarServiceServiceImpl service;

    private CarService buildCar() {
        return CarService.builder()
                .id(1L)
                .customerId(10L)
                .carRegistrationNumber("TN10AB1234")
                .serviceType(com.cognizant.carservice.enums.ServiceType.GENERAL_SERVICE)
                .serviceStatus(com.cognizant.carservice.enums.ServiceStatus.PENDING)
                .serviceDate(LocalDate.now())
                .notes("test")
                .build();
    }

    // CREATE SUCCESS
    @Test
    void testAddCarServiceSuccess() {
        CarService car = buildCar();

        when(userServiceClient.userExists(10L)).thenReturn(true);
        when(repository.existsByCarRegistrationNumber("TN10AB1234")).thenReturn(false);
        when(carValidationClient.isCarValid("TN10AB1234")).thenReturn(true);
        when(repository.save(any(CarService.class))).thenReturn(car);

        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        CarService saved = service.addCarService(car);

        assertNotNull(saved);
        verify(repository).save(car);
        verify(auditClient).sendAudit(anyLong(), eq("CAR_CREATED"), anyString(), anyString());
    }

    // USER NOT FOUND
    @Test
    void testAddCarServiceUserNotFound() {
        CarService car = buildCar();

        when(userServiceClient.userExists(10L)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () -> service.addCarService(car));
    }

    // DUPLICATE CAR
    @Test
    void testAddCarServiceDuplicate() {
        CarService car = buildCar();

        when(userServiceClient.userExists(10L)).thenReturn(true);
        when(repository.existsByCarRegistrationNumber("TN10AB1234")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.addCarService(car));
    }

    // INVALID CAR
    @Test
    void testAddCarServiceInvalidCar() {
        CarService car = buildCar();

        when(userServiceClient.userExists(10L)).thenReturn(true);
        when(repository.existsByCarRegistrationNumber("TN10AB1234")).thenReturn(false);
        when(carValidationClient.isCarValid("TN10AB1234")).thenReturn(false);

        assertThrows(InvalidCarException.class, () -> service.addCarService(car));
    }

    // GET ALL
    @Test
    void testGetAll() {
        when(repository.findAll()).thenReturn(List.of(buildCar()));

        List<CarService> list = service.getAllCarServices();
        assertEquals(1, list.size());
    }

    // GET BY ID SUCCESS
    @Test
    void testGetById() {
        when(repository.findById(1L)).thenReturn(Optional.of(buildCar()));

        CarService car = service.getCarServiceById(1L);
        assertNotNull(car);
    }

    // GET BY ID FAIL
    @Test
    void testGetByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getCarServiceById(1L));
    }

    // UPDATE SUCCESS
    @Test
    void testUpdate() {
        CarService existing = buildCar();
        CarService update = buildCar();
        update.setNotes("updated");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);

        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        CarService result = service.updateCarService(1L, update);

        assertEquals("updated", result.getNotes());
        verify(auditClient).sendAudit(anyLong(), eq("CAR_UPDATED"), anyString(), anyString());
    }

    // DELETE SUCCESS
    @Test
    void testDelete() {
        CarService existing = buildCar();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(repository).delete(existing);
        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        service.deleteCarService(1L);

        verify(repository).delete(existing);
        verify(auditClient).sendAudit(anyLong(), eq("CAR_DELETED"), anyString(), anyString());
    }
}