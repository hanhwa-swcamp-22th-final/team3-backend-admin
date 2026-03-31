package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EnvironmentEventQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EnvironmentStandardQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentProcessQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.FactoryLineQueryService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EquipmentManageQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class EnvironmentEventQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FactoryLineQueryService factoryLineQueryService;

    @MockitoBean
    private EquipmentProcessQueryService equipmentProcessQueryService;

    @MockitoBean
    private EquipmentQueryService equipmentQueryService;

    @MockitoBean
    private EnvironmentStandardQueryService environmentStandardQueryService;

    @MockitoBean
    private EnvironmentEventQueryService environmentEventQueryService;

    @Test
    @DisplayName("Get environment event list API success: return list JSON")
    void getEnvironmentEventList_success() throws Exception {
        EnvironmentEventQueryResponse response = new EnvironmentEventQueryResponse();
        response.setEnvironmentEventId(1L);
        response.setEquipmentId(10L);
        response.setEquipmentCode("EQ-001");
        response.setEquipmentName("Drying Equipment");
        response.setEnvDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION);
        response.setEnvCorrectionApplied(false);

        when(environmentEventQueryService.getEnvironmentEventList(argThat(request -> request != null)))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/environment-events"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].environmentEventId").value(1L))
            .andExpect(jsonPath("$.data[0].equipmentId").value(10L))
            .andExpect(jsonPath("$.data[0].envDeviationType").value("TEMPERATURE_DEVIATION"));
    }

    @Test
    @DisplayName("Get environment event list API success: bind query parameters correctly")
    void getEnvironmentEventList_withQueryParams_success() throws Exception {
        when(environmentEventQueryService.getEnvironmentEventList(argThat(request -> request != null)))
            .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/equipment-management/environment-events")
                .param("equipmentId", "10")
                .param("envDeviationType", "TEMPERATURE_DEVIATION"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(environmentEventQueryService).getEnvironmentEventList(argThat(request ->
            Long.valueOf(10L).equals(request.getEquipmentId())
                && EnvDeviationType.TEMPERATURE_DEVIATION == request.getEnvDeviationType()
        ));
    }

    @Test
    @DisplayName("Get environment event detail API success: return detail JSON")
    void getEnvironmentEventDetail_success() throws Exception {
        EnvironmentEventDetailResponse response = new EnvironmentEventDetailResponse();
        response.setEnvironmentEventId(1L);
        response.setEquipmentId(10L);
        response.setEquipmentCode("EQ-001");
        response.setEquipmentName("Drying Equipment");
        response.setEnvDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION);
        response.setEnvCorrectionApplied(false);

        when(environmentEventQueryService.getEnvironmentEventDetail(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/environment-events/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.environmentEventId").value(1L))
            .andExpect(jsonPath("$.data.equipmentId").value(10L));
    }

    @Test
    @DisplayName("Get environment event detail failure: propagate service exception")
    void getEnvironmentEventDetail_whenServiceThrows_thenPropagatesServletException() {
        when(environmentEventQueryService.getEnvironmentEventDetail(999L))
            .thenThrow(new IllegalArgumentException("Environment event not found."));

        ServletException exception = assertThrows(ServletException.class,
            () -> mockMvc.perform(get("/api/v1/equipment-management/environment-events/999")).andReturn());

        verify(environmentEventQueryService).getEnvironmentEventDetail(999L);
    }
}