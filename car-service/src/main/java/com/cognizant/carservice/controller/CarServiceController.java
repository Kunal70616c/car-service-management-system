package com.cognizant.carservice.controller;

import com.cognizant.carservice.dto.CarServiceDTO;
import com.cognizant.carservice.dto.CarServiceResponse;
import com.cognizant.carservice.dto.GenericResponse;
import com.cognizant.carservice.mapper.CarServiceMapper;
import com.cognizant.carservice.model.CarService;
import com.cognizant.carservice.service.CarServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carservice")
@RequiredArgsConstructor
@Slf4j
public class CarServiceController {

    private final CarServiceService service;
    private final CarServiceMapper mapper;

    // CREATE
    @PostMapping("/save")
    public ResponseEntity<GenericResponse<CarServiceResponse>> create(@Valid @RequestBody CarServiceDTO dto) {

        log.info("DTO RECEIVED: " + dto);

        CarService entity = mapper.dtoToEntity(dto);

        log.info("ENTITY AFTER MAPPING: " + entity);

        CarService saved = service.addCarService(entity);


        return ResponseEntity.status(201)
                .body(new GenericResponse<>("Car created", mapper.entityToResponse(saved)));
    }

    // GET ALL
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('SCOPE_admin','SCOPE_user')")
    public ResponseEntity<GenericResponse<List<CarServiceResponse>>> getAll() {

        List<CarService> list = service.getAllCarServices();
        List<CarServiceResponse> response = mapper.entityListToResponseList(list);

        return ResponseEntity.ok(new GenericResponse<>("All cars fetched", response));
    }

    // GET BY ID
    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_admin','SCOPE_user')")
    public ResponseEntity<GenericResponse<CarServiceResponse>> getById(@PathVariable Long id) {

        CarService car = service.getCarServiceById(id);

        return ResponseEntity.ok(
                new GenericResponse<>("Car fetched", mapper.entityToResponse(car))
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_admin')")
    public ResponseEntity<GenericResponse<CarServiceResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CarServiceDTO dto) {

        CarService updated = service.updateCarService(id, mapper.dtoToEntity(dto));

        return ResponseEntity.ok(
                new GenericResponse<>("Car updated", mapper.entityToResponse(updated))
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_admin')")
    public ResponseEntity<GenericResponse<String>> delete(@PathVariable Long id) {

        service.deleteCarService(id);
        return ResponseEntity.status(202)
                .body(new GenericResponse<>("Car deleted successfully"));
    }
}