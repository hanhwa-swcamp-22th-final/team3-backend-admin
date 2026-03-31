package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineQueryResponse;
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

import java.time.LocalDate;
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
class EquipmentManageQueryControllerTest {

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
    @DisplayName("Get factory line list API success: return list JSON")
    void getFactoryLineList_success() throws Exception {
        FactoryLineQueryResponse response = new FactoryLineQueryResponse();
        response.setFactoryLineId(1L);
        response.setFactoryLineCode("LINE-001");
        response.setFactoryLineName("Main Line");

        when(factoryLineQueryService.getFactoryLineList(argThat(request -> request != null))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/factory-lines"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].factoryLineId").value(1L));
    }

    @Test
    @DisplayName("Get factory line list API success: bind query parameters correctly")
    void getFactoryLineList_withQueryParams_success() throws Exception {
        when(factoryLineQueryService.getFactoryLineList(argThat(request -> request != null))).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/equipment-management/factory-lines").param("keyword", "main"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(factoryLineQueryService).getFactoryLineList(argThat(request -> "main".equals(request.getKeyword())));
    }

    @Test
    @DisplayName("Get factory line detail API success: return detail JSON")
    void getFactoryLineDetail_success() throws Exception {
        FactoryLineDetailResponse response = new FactoryLineDetailResponse();
        response.setFactoryLineId(1L);
        response.setFactoryLineCode("LINE-001");
        response.setFactoryLineName("Main Line");

        when(factoryLineQueryService.getFactoryLineDetail(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/factory-lines/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineId").value(1L));
    }

    @Test
    @DisplayName("Get factory line detail API failure: propagate exception when the target does not exist")
    void getFactoryLineDetail_whenServiceThrows_thenPropagatesServletException() {
        when(factoryLineQueryService.getFactoryLineDetail(999L)).thenThrow(new IllegalArgumentException("Factory line not found."));

        assertThrows(ServletException.class,
            () -> mockMvc.perform(get("/api/v1/equipment-management/factory-lines/999")));
    }

    @Test
    @DisplayName("Get equipment process list API success: return list JSON")
    void getEquipmentProcessList_success() throws Exception {
        EquipmentProcessQueryResponse response = new EquipmentProcessQueryResponse();
        response.setEquipmentProcessId(1L);
        response.setFactoryLineId(10L);
        response.setFactoryLineCode("LINE-001");
        response.setFactoryLineName("Main Line");
        response.setEquipmentProcessCode("PROC-001");
        response.setEquipmentProcessName("Mixing Process");

        when(equipmentProcessQueryService.getEquipmentProcessList(argThat(request -> request != null))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/equipment-processes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].equipmentProcessId").value(1L));
    }

    @Test
    @DisplayName("Get equipment process list API success: bind query parameters correctly")
    void getEquipmentProcessList_withQueryParams_success() throws Exception {
        when(equipmentProcessQueryService.getEquipmentProcessList(argThat(request -> request != null))).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/equipment-management/equipment-processes")
                .param("factoryLineId", "10")
                .param("keyword", "mix"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(equipmentProcessQueryService).getEquipmentProcessList(argThat(request ->
            Long.valueOf(10L).equals(request.getFactoryLineId()) && "mix".equals(request.getKeyword())
        ));
    }

    @Test
    @DisplayName("Get equipment process detail API success: return detail JSON")
    void getEquipmentProcessDetail_success() throws Exception {
        EquipmentProcessDetailResponse response = new EquipmentProcessDetailResponse();
        response.setEquipmentProcessId(1L);
        response.setFactoryLineId(10L);
        response.setFactoryLineCode("LINE-001");
        response.setFactoryLineName("Main Line");
        response.setEquipmentProcessCode("PROC-001");
        response.setEquipmentProcessName("Mixing Process");

        when(equipmentProcessQueryService.getEquipmentProcessDetail(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/equipment-processes/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessId").value(1L));
    }

    @Test
    @DisplayName("Get equipment process detail API failure: propagate exception when the target does not exist")
    void getEquipmentProcessDetail_whenServiceThrows_thenPropagatesServletException() {
        when(equipmentProcessQueryService.getEquipmentProcessDetail(999L)).thenThrow(new IllegalArgumentException("Equipment process not found."));

        assertThrows(ServletException.class,
            () -> mockMvc.perform(get("/api/v1/equipment-management/equipment-processes/999")));
    }

    @Test
    @DisplayName("Get environment standard list API success: return list JSON")
    void getEnvironmentStandardList_success() throws Exception {
        EnvironmentStandardQueryResponse response = new EnvironmentStandardQueryResponse();
        response.setEnvironmentStandardId(1L);
        response.setEnvironmentType(EnvironmentType.DRYROOM);
        response.setEnvironmentCode("ENV-001");
        response.setEnvironmentName("Dry Room");

        when(environmentStandardQueryService.getEnvironmentStandardList(argThat(request -> request != null))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/environment-standards"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].environmentStandardId").value(1L));
    }

    @Test
    @DisplayName("Get environment standard list API success: bind query parameters correctly")
    void getEnvironmentStandardList_withQueryParams_success() throws Exception {
        when(environmentStandardQueryService.getEnvironmentStandardList(argThat(request -> request != null))).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/equipment-management/environment-standards")
                .param("keyword", "dry")
                .param("environmentType", "DRYROOM"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(environmentStandardQueryService).getEnvironmentStandardList(argThat(request ->
            "dry".equals(request.getKeyword()) && EnvironmentType.DRYROOM == request.getEnvironmentType()
        ));
    }

    @Test
    @DisplayName("Get environment standard detail API success: return detail JSON")
    void getEnvironmentStandardDetail_success() throws Exception {
        EnvironmentStandardDetailResponse response = new EnvironmentStandardDetailResponse();
        response.setEnvironmentStandardId(1L);
        response.setEnvironmentType(EnvironmentType.DRYROOM);
        response.setEnvironmentCode("ENV-001");
        response.setEnvironmentName("Dry Room");

        when(environmentStandardQueryService.getEnvironmentStandardDetail(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/environment-standards/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.environmentStandardId").value(1L));
    }

    @Test
    @DisplayName("Get environment standard detail failure: propagate service exception")
    void getEnvironmentStandardDetail_whenServiceThrows_thenPropagatesServletException() {
        when(environmentStandardQueryService.getEnvironmentStandardDetail(999L)).thenThrow(new IllegalArgumentException("Environment standard not found."));

        assertThrows(ServletException.class,
            () -> mockMvc.perform(get("/api/v1/equipment-management/environment-standards/999")).andReturn());
    }

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

        when(environmentEventQueryService.getEnvironmentEventList(argThat(request -> request != null))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/environment-events"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].environmentEventId").value(1L));
    }

    @Test
    @DisplayName("Get environment event list API success: bind query parameters correctly")
    void getEnvironmentEventList_withQueryParams_success() throws Exception {
        when(environmentEventQueryService.getEnvironmentEventList(argThat(request -> request != null))).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/equipment-management/environment-events")
                .param("equipmentId", "10")
                .param("envDeviationType", "TEMPERATURE_DEVIATION"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(environmentEventQueryService).getEnvironmentEventList(argThat(request ->
            Long.valueOf(10L).equals(request.getEquipmentId()) && EnvDeviationType.TEMPERATURE_DEVIATION == request.getEnvDeviationType()
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
            .andExpect(jsonPath("$.data.environmentEventId").value(1L));
    }

    @Test
    @DisplayName("Get environment event detail failure: propagate service exception")
    void getEnvironmentEventDetail_whenServiceThrows_thenPropagatesServletException() {
        when(environmentEventQueryService.getEnvironmentEventDetail(999L)).thenThrow(new IllegalArgumentException("Environment event not found."));

        assertThrows(ServletException.class,
            () -> mockMvc.perform(get("/api/v1/equipment-management/environment-events/999")).andReturn());
    }

    @Test
    @DisplayName("Get equipment list API success: return list JSON")
    void getEquipments_success() throws Exception {
        EquipmentQueryResponse response = new EquipmentQueryResponse();
        response.setEquipmentId(1L);
        response.setEquipmentCode("EQ-001");
        response.setEquipmentName("Printer");
        response.setEquipmentStatus(EquipmentStatus.OPERATING);
        response.setEquipmentGrade(EquipmentGrade.S);

        when(equipmentQueryService.getEquipmentList(argThat(request -> request != null))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/equipments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].equipmentId").value(1L));
    }

    @Test
    @DisplayName("Get equipment list API success: bind query parameters correctly")
    void getEquipments_withQueryParams_success() throws Exception {
        when(equipmentQueryService.getEquipmentList(argThat(request -> request != null))).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/equipment-management/equipments")
                .param("keyword", "printer")
                .param("equipmentStatus", "OPERATING")
                .param("equipmentGrade", "S"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(equipmentQueryService).getEquipmentList(argThat(request ->
            "printer".equals(request.getKeyword())
                && request.getEquipmentStatus() == EquipmentStatus.OPERATING
                && request.getEquipmentGrade() == EquipmentGrade.S
        ));
    }

    @Test
    @DisplayName("Get equipment detail API success: return detail JSON")
    void getEquipmentDetail_success() throws Exception {
        EquipmentDetailResponse response = new EquipmentDetailResponse();
        response.setEquipmentId(1L);
        response.setEquipmentCode("EQ-001");
        response.setEquipmentName("Printer");
        response.setEquipmentStatus(EquipmentStatus.OPERATING);
        response.setEquipmentGrade(EquipmentGrade.S);
        response.setEquipmentInstallDate(LocalDate.of(2025, 1, 15).atStartOfDay());
        response.setEquipmentDescription("Main line printer");

        when(equipmentQueryService.getEquipmentDetail(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/equipments/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentId").value(1L));
    }

    @Test
    @DisplayName("Get equipment detail API failure: propagate exception when the target does not exist")
    void getEquipmentDetail_whenServiceThrows_thenPropagatesServletException() {
        when(equipmentQueryService.getEquipmentDetail(999L)).thenThrow(new IllegalArgumentException("Equipment not found."));

        assertThrows(ServletException.class,
            () -> mockMvc.perform(get("/api/v1/equipment-management/equipments/999")));
    }
}
