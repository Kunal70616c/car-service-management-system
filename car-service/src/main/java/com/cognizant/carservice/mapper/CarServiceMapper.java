package com.cognizant.carservice.mapper;

import com.cognizant.carservice.dto.CarServiceDTO;
import com.cognizant.carservice.dto.CarServiceResponse;
import com.cognizant.carservice.model.CarService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarServiceMapper {

    // DTO → Entity
    CarService dtoToEntity(CarServiceDTO dto);

    // Entity → DTO
    CarServiceDTO entityToDto(CarService entity);

    // Entity → Response record
    CarServiceResponse entityToResponse(CarService entity);

    // List mapping
    List<CarServiceResponse> entityListToResponseList(List<CarService> list);

    // update existing entity from dto
    void updateEntityFromDto(CarServiceDTO dto, @MappingTarget CarService entity);
}