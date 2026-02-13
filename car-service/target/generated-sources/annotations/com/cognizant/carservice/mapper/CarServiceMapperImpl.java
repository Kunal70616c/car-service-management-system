package com.cognizant.carservice.mapper;

import com.cognizant.carservice.dto.CarServiceDTO;
import com.cognizant.carservice.dto.CarServiceResponse;
import com.cognizant.carservice.enums.ServiceStatus;
import com.cognizant.carservice.enums.ServiceType;
import com.cognizant.carservice.model.CarService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-14T01:44:27+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.10 (Homebrew)"
)
@Component
public class CarServiceMapperImpl implements CarServiceMapper {

    @Override
    public CarService dtoToEntity(CarServiceDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CarService.CarServiceBuilder carService = CarService.builder();

        carService.id( dto.getId() );
        carService.customerId( dto.getCustomerId() );
        carService.carRegistrationNumber( dto.getCarRegistrationNumber() );
        carService.serviceType( dto.getServiceType() );
        carService.serviceStatus( dto.getServiceStatus() );
        carService.serviceDate( dto.getServiceDate() );
        carService.notes( dto.getNotes() );
        carService.createdAt( dto.getCreatedAt() );
        carService.updatedAt( dto.getUpdatedAt() );

        return carService.build();
    }

    @Override
    public CarServiceDTO entityToDto(CarService entity) {
        if ( entity == null ) {
            return null;
        }

        CarServiceDTO.CarServiceDTOBuilder carServiceDTO = CarServiceDTO.builder();

        carServiceDTO.id( entity.getId() );
        carServiceDTO.customerId( entity.getCustomerId() );
        carServiceDTO.carRegistrationNumber( entity.getCarRegistrationNumber() );
        carServiceDTO.serviceType( entity.getServiceType() );
        carServiceDTO.serviceStatus( entity.getServiceStatus() );
        carServiceDTO.serviceDate( entity.getServiceDate() );
        carServiceDTO.notes( entity.getNotes() );
        carServiceDTO.createdAt( entity.getCreatedAt() );
        carServiceDTO.updatedAt( entity.getUpdatedAt() );

        return carServiceDTO.build();
    }

    @Override
    public CarServiceResponse entityToResponse(CarService entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        Long customerId = null;
        String carRegistrationNumber = null;
        ServiceType serviceType = null;
        ServiceStatus serviceStatus = null;
        LocalDate serviceDate = null;
        String notes = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = entity.getId();
        customerId = entity.getCustomerId();
        carRegistrationNumber = entity.getCarRegistrationNumber();
        serviceType = entity.getServiceType();
        serviceStatus = entity.getServiceStatus();
        serviceDate = entity.getServiceDate();
        notes = entity.getNotes();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        CarServiceResponse carServiceResponse = new CarServiceResponse( id, customerId, carRegistrationNumber, serviceType, serviceStatus, serviceDate, notes, createdAt, updatedAt );

        return carServiceResponse;
    }

    @Override
    public List<CarServiceResponse> entityListToResponseList(List<CarService> list) {
        if ( list == null ) {
            return null;
        }

        List<CarServiceResponse> list1 = new ArrayList<CarServiceResponse>( list.size() );
        for ( CarService carService : list ) {
            list1.add( entityToResponse( carService ) );
        }

        return list1;
    }

    @Override
    public void updateEntityFromDto(CarServiceDTO dto, CarService entity) {
        if ( dto == null ) {
            return;
        }

        entity.setId( dto.getId() );
        entity.setCustomerId( dto.getCustomerId() );
        entity.setCarRegistrationNumber( dto.getCarRegistrationNumber() );
        entity.setServiceType( dto.getServiceType() );
        entity.setServiceStatus( dto.getServiceStatus() );
        entity.setServiceDate( dto.getServiceDate() );
        entity.setNotes( dto.getNotes() );
        entity.setCreatedAt( dto.getCreatedAt() );
        entity.setUpdatedAt( dto.getUpdatedAt() );
    }
}
