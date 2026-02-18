package com.cognizant.carservice.controller;

import com.cognizant.carservice.dto.CarServiceDTO;
import com.cognizant.carservice.dto.CarServiceResponse;
import com.cognizant.carservice.enums.ServiceStatus;
import com.cognizant.carservice.enums.ServiceType;
import com.cognizant.carservice.mapper.CarServiceMapper;
import com.cognizant.carservice.model.CarService;
import com.cognizant.carservice.service.CarServiceService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarServiceController.class)
@AutoConfigureMockMvc(addFilters = false)
class CarServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarServiceService service;

    @MockitoBean
    private CarServiceMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private CarServiceResponse buildResponse() {
        return new CarServiceResponse(
                1L,
                1L,
                "TN10AB1234",
                ServiceType.GENERAL_SERVICE,
                ServiceStatus.PENDING,
                LocalDate.now(),
                "notes",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @WithMockUser(authorities = "SCOPE_admin")
    void testCreate() throws Exception {

        CarServiceDTO dto = new CarServiceDTO();
        dto.setCustomerId(1L);
        dto.setCarRegistrationNumber("TN10AB1234");
        dto.setServiceDate(LocalDate.now());

        CarService entity = new CarService();
        entity.setId(1L);

        when(mapper.dtoToEntity(any())).thenReturn(entity);
        when(service.addCarService(any())).thenReturn(entity);
        when(mapper.entityToResponse(any())).thenReturn(buildResponse());

        mockMvc.perform(post("/carservice/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_admin","SCOPE_user"})
    void testGetAll() throws Exception {

        when(service.getAllCarServices()).thenReturn(List.of(new CarService()));
        when(mapper.entityListToResponseList(any())).thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/carservice/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_admin","SCOPE_user"})
    void testGetById() throws Exception {

        when(service.getCarServiceById(1L)).thenReturn(new CarService());
        when(mapper.entityToResponse(any())).thenReturn(buildResponse());

        mockMvc.perform(get("/carservice/get/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_admin")
    void testUpdate() throws Exception {

        CarServiceDTO dto = new CarServiceDTO();
        dto.setServiceDate(LocalDate.now());

        when(mapper.dtoToEntity(any())).thenReturn(new CarService());
        when(service.updateCarService(any(), any())).thenReturn(new CarService());
        when(mapper.entityToResponse(any())).thenReturn(buildResponse());

        mockMvc.perform(put("/carservice/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_admin")
    void testDelete() throws Exception {

        doNothing().when(service).deleteCarService(1L);

        mockMvc.perform(delete("/carservice/1"))
                .andExpect(status().isAccepted());
    }
}