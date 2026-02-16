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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Car Service Service Implementation Tests")
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

    private CarService buildCarWithId(Long id) {
        return CarService.builder()
                .id(id)
                .customerId(10L)
                .carRegistrationNumber("TN10AB" + id)
                .serviceType(com.cognizant.carservice.enums.ServiceType.GENERAL_SERVICE)
                .serviceStatus(com.cognizant.carservice.enums.ServiceStatus.PENDING)
                .serviceDate(LocalDate.now())
                .notes("test")
                .build();
    }

    // ==================== CREATE TESTS ====================

    @Test
    @Tag("create")
    @Tag("success")
    @DisplayName("Add Car Service - Success")
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

    @RepeatedTest(value = 3, name = "Repetition {currentRepetition} of {totalRepetitions}")
    @Tag("create")
    @Tag("success")
    @DisplayName("Add Car Service - Repeated Test for Reliability")
    void testAddCarServiceSuccessRepeated(RepetitionInfo repetitionInfo) {
        CarService car = buildCar();
        car.setId((long) repetitionInfo.getCurrentRepetition());

        when(userServiceClient.userExists(10L)).thenReturn(true);
        when(repository.existsByCarRegistrationNumber(anyString())).thenReturn(false);
        when(carValidationClient.isCarValid(anyString())).thenReturn(true);
        when(repository.save(any(CarService.class))).thenReturn(car);
        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        CarService saved = service.addCarService(car);

        assertNotNull(saved);
        assertEquals(repetitionInfo.getCurrentRepetition(), saved.getId());
        verify(repository).save(car);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 5L, 10L, 15L, 20L})
    @Tag("create")
    @Tag("validation")
    @DisplayName("Add Car Service - Parameterized Test with Different Customer IDs")
    void testAddCarServiceWithDifferentCustomerIds(Long customerId) {
        CarService car = buildCar();
        car.setCustomerId(customerId);

        when(userServiceClient.userExists(customerId)).thenReturn(true);
        when(repository.existsByCarRegistrationNumber("TN10AB1234")).thenReturn(false);
        when(carValidationClient.isCarValid("TN10AB1234")).thenReturn(true);
        when(repository.save(any(CarService.class))).thenReturn(car);
        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        CarService saved = service.addCarService(car);

        assertNotNull(saved);
        assertEquals(customerId, saved.getCustomerId());
        verify(userServiceClient).userExists(customerId);
    }

    @ParameterizedTest
    @ValueSource(strings = {"TN10AB1234", "KA05CD5678", "MH12EF9012", "DL01GH3456"})
    @Tag("create")
    @Tag("validation")
    @DisplayName("Add Car Service - Parameterized Test with Different Registration Numbers")
    void testAddCarServiceWithDifferentRegistrationNumbers(String regNumber) {
        CarService car = buildCar();
        car.setCarRegistrationNumber(regNumber);

        when(userServiceClient.userExists(10L)).thenReturn(true);
        when(repository.existsByCarRegistrationNumber(regNumber)).thenReturn(false);
        when(carValidationClient.isCarValid(regNumber)).thenReturn(true);
        when(repository.save(any(CarService.class))).thenReturn(car);
        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        CarService saved = service.addCarService(car);

        assertNotNull(saved);
        assertEquals(regNumber, saved.getCarRegistrationNumber());
        verify(carValidationClient).isCarValid(regNumber);
    }

    @Test
    @Tag("create")
    @Tag("exception")
    @DisplayName("Add Car Service - User Not Found Exception")
    void testAddCarServiceUserNotFound() {
        CarService car = buildCar();
        when(userServiceClient.userExists(10L)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () -> service.addCarService(car));
        verify(repository, never()).save(any());
    }

    @ParameterizedTest
    @ValueSource(longs = {100L, 200L, 300L})
    @Tag("create")
    @Tag("exception")
    @DisplayName("Add Car Service - User Not Found with Different Customer IDs")
    void testAddCarServiceUserNotFoundParameterized(Long customerId) {
        CarService car = buildCar();
        car.setCustomerId(customerId);
        when(userServiceClient.userExists(customerId)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () -> service.addCarService(car));
        verify(repository, never()).save(any());
    }

    @Test
    @Tag("create")
    @Tag("exception")
    @DisplayName("Add Car Service - Duplicate Resource Exception")
    void testAddCarServiceDuplicate() {
        CarService car = buildCar();

        when(userServiceClient.userExists(10L)).thenReturn(true);
        when(repository.existsByCarRegistrationNumber("TN10AB1234")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.addCarService(car));
        verify(carValidationClient, never()).isCarValid(anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"TN10AB1234", "KA05CD5678", "MH12EF9012"})
    @Tag("create")
    @Tag("exception")
    @DisplayName("Add Car Service - Duplicate with Different Registration Numbers")
    void testAddCarServiceDuplicateParameterized(String regNumber) {
        CarService car = buildCar();
        car.setCarRegistrationNumber(regNumber);

        when(userServiceClient.userExists(10L)).thenReturn(true);
        when(repository.existsByCarRegistrationNumber(regNumber)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.addCarService(car));
        verify(repository, never()).save(any());
    }

    @Test
    @Tag("create")
    @Tag("exception")
    @DisplayName("Add Car Service - Invalid Car Exception")
    void testAddCarServiceInvalidCar() {
        CarService car = buildCar();

        when(userServiceClient.userExists(10L)).thenReturn(true);
        when(repository.existsByCarRegistrationNumber("TN10AB1234")).thenReturn(false);
        when(carValidationClient.isCarValid("TN10AB1234")).thenReturn(false);

        assertThrows(InvalidCarException.class, () -> service.addCarService(car));
        verify(repository, never()).save(any());
    }

    @RepeatedTest(3)
    @Tag("create")
    @Tag("exception")
    @DisplayName("Add Car Service - Invalid Car Repeated Test")
    void testAddCarServiceInvalidCarRepeated() {
        CarService car = buildCar();

        when(userServiceClient.userExists(10L)).thenReturn(true);
        when(repository.existsByCarRegistrationNumber("TN10AB1234")).thenReturn(false);
        when(carValidationClient.isCarValid("TN10AB1234")).thenReturn(false);

        assertThrows(InvalidCarException.class, () -> service.addCarService(car));
    }

    // ==================== READ TESTS ====================

    @Test
    @Tag("read")
    @Tag("success")
    @DisplayName("Get All Car Services")
    void testGetAll() {
        when(repository.findAll()).thenReturn(List.of(buildCar()));

        List<CarService> list = service.getAllCarServices();

        assertEquals(1, list.size());
        verify(repository).findAll();
    }

    @RepeatedTest(3)
    @Tag("read")
    @Tag("success")
    @DisplayName("Get All Car Services - Repeated Test")
    void testGetAllRepeated() {
        when(repository.findAll()).thenReturn(List.of(buildCar()));

        List<CarService> list = service.getAllCarServices();

        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @Tag("read")
    @Tag("success")
    @DisplayName("Get Car Service By ID - Success")
    void testGetById() {
        when(repository.findById(1L)).thenReturn(Optional.of(buildCar()));

        CarService car = service.getCarServiceById(1L);

        assertNotNull(car);
        assertEquals(1L, car.getId());
        verify(repository).findById(1L);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    @Tag("read")
    @Tag("success")
    @DisplayName("Get Car Service By ID - Parameterized Test")
    void testGetByIdParameterized(Long id) {
        CarService car = buildCarWithId(id);
        when(repository.findById(id)).thenReturn(Optional.of(car));

        CarService result = service.getCarServiceById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository).findById(id);
    }

    @Test
    @Tag("read")
    @Tag("exception")
    @DisplayName("Get Car Service By ID - Not Found Exception")
    void testGetByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getCarServiceById(1L));
        verify(repository).findById(1L);
    }

    @ParameterizedTest
    @ValueSource(longs = {99L, 100L, 500L, 1000L})
    @Tag("read")
    @Tag("exception")
    @DisplayName("Get Car Service By ID - Not Found with Different IDs")
    void testGetByIdNotFoundParameterized(Long id) {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getCarServiceById(id));
    }

    // ==================== UPDATE TESTS ====================

    @Test
    @Tag("update")
    @Tag("success")
    @DisplayName("Update Car Service - Success")
    void testUpdate() {
        CarService existing = buildCar();
        CarService update = buildCar();
        update.setNotes("updated");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);
        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        CarService result = service.updateCarService(1L, update);

        assertEquals("updated", result.getNotes());
        verify(repository).save(any());
        verify(auditClient).sendAudit(anyLong(), eq("CAR_UPDATED"), anyString(), anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Updated notes 1", "Updated notes 2", "Updated notes 3"})
    @Tag("update")
    @Tag("success")
    @DisplayName("Update Car Service - Parameterized Notes Update")
    void testUpdateWithDifferentNotes(String notes) {
        CarService existing = buildCar();
        CarService update = buildCar();
        update.setNotes(notes);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> {
            CarService saved = invocation.getArgument(0);
            saved.setNotes(notes);
            return saved;
        });
        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        CarService result = service.updateCarService(1L, update);

        assertEquals(notes, result.getNotes());
        verify(auditClient).sendAudit(anyLong(), eq("CAR_UPDATED"), anyString(), anyString());
    }

    @RepeatedTest(3)
    @Tag("update")
    @Tag("success")
    @DisplayName("Update Car Service - Repeated Test")
    void testUpdateRepeated() {
        CarService existing = buildCar();
        CarService update = buildCar();
        update.setNotes("repeated update");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);
        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        CarService result = service.updateCarService(1L, update);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    @Tag("update")
    @Tag("success")
    @DisplayName("Update Car Service - Different IDs")
    void testUpdateWithDifferentIds(Long id) {
        CarService existing = buildCarWithId(id);
        CarService update = buildCarWithId(id);
        update.setNotes("updated for id " + id);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenReturn(existing);
        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        CarService result = service.updateCarService(id, update);

        assertNotNull(result);
        verify(repository).findById(id);
    }

    // ==================== DELETE TESTS ====================

    @Test
    @Tag("delete")
    @Tag("success")
    @DisplayName("Delete Car Service - Success")
    void testDelete() {
        CarService existing = buildCar();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(repository).delete(existing);
        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        service.deleteCarService(1L);

        verify(repository).delete(existing);
        verify(auditClient).sendAudit(anyLong(), eq("CAR_DELETED"), anyString(), anyString());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 5L, 10L, 15L})
    @Tag("delete")
    @Tag("success")
    @DisplayName("Delete Car Service - Parameterized Test")
    void testDeleteParameterized(Long id) {
        CarService existing = buildCarWithId(id);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(repository).delete(existing);
        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        service.deleteCarService(id);

        verify(repository).delete(existing);
        verify(auditClient).sendAudit(anyLong(), eq("CAR_DELETED"), anyString(), anyString());
    }

    @RepeatedTest(3)
    @Tag("delete")
    @Tag("success")
    @DisplayName("Delete Car Service - Repeated Test")
    void testDeleteRepeated() {
        CarService existing = buildCar();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(repository).delete(existing);
        doNothing().when(auditClient).sendAudit(anyLong(), anyString(), anyString(), anyString());

        service.deleteCarService(1L);

        verify(repository).delete(existing);
    }

    @ParameterizedTest
    @ValueSource(longs = {999L, 1000L, 5000L})
    @Tag("delete")
    @Tag("exception")
    @DisplayName("Delete Car Service - Not Found Exception")
    void testDeleteNotFound(Long id) {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCarService(id));
        verify(repository, never()).delete(any());
    }
}