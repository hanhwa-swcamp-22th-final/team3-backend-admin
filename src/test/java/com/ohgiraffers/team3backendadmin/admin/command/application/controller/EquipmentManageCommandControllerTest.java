package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentEventCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentEventUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentStandardCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentStandardUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentEventCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentEventUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentStandardCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentStandardUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentProcessManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EnvironmentEventManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EnvironmentStandardManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.FactoryLineManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EquipmentManageCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
class EquipmentManageCommandControllerTest {

    private static final String BASE_URL = "/api/v1/equipment-management";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FactoryLineManageCommandService factoryLineManageCommandService;

    @MockitoBean
    private EquipmentProcessManageCommandService equipmentProcessManageCommandService;

    @MockitoBean
    private EquipmentManageCommandService equipmentManageCommandService;

    @MockitoBean
    private EnvironmentStandardManageCommandService environmentStandardManageCommandService;

    @MockitoBean
    private EnvironmentEventManageCommandService environmentEventManageCommandService;

    @Test
    @DisplayName("Create environment standard API success: return a successful response")
    void createEnvironmentStandard_success() throws Exception {
        EnvironmentStandardCreateRequest request = createEnvironmentStandardCreateRequest();

        EnvironmentStandardCreateResponse response = EnvironmentStandardCreateResponse.builder()
            .environmentStandardId(3001L)
            .environmentType(EnvironmentType.DRYROOM)
            .environmentCode("ENV-001")
            .environmentName("Dry Room")
            .build();

        when(environmentStandardManageCommandService.createEnvironmentStandard(any(EnvironmentStandardCreateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/environment-standards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.environmentStandardId").value(3001L))
            .andExpect(jsonPath("$.data.environmentType").value("DRYROOM"))
            .andExpect(jsonPath("$.data.environmentCode").value("ENV-001"))
            .andExpect(jsonPath("$.data.environmentName").value("Dry Room"));

        verify(environmentStandardManageCommandService).createEnvironmentStandard(any(EnvironmentStandardCreateRequest.class));
    }

    @Test
    @DisplayName("Update environment standard API success: return a successful response")
    void updateEnvironmentStandard_success() throws Exception {
        EnvironmentStandardUpdateRequest request = createEnvironmentStandardUpdateRequest();

        EnvironmentStandardUpdateResponse response = EnvironmentStandardUpdateResponse.builder()
            .environmentStandardId(3001L)
            .environmentType(EnvironmentType.CLEANROOM)
            .environmentCode("ENV-999")
            .environmentName("Clean Room")
            .build();

        when(environmentStandardManageCommandService.updateEnvironmentStandard(eq(3001L), any(EnvironmentStandardUpdateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(put(BASE_URL + "/environment-standards/3001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.environmentStandardId").value(3001L))
            .andExpect(jsonPath("$.data.environmentType").value("CLEANROOM"))
            .andExpect(jsonPath("$.data.environmentCode").value("ENV-999"));

        verify(environmentStandardManageCommandService).updateEnvironmentStandard(eq(3001L), any(EnvironmentStandardUpdateRequest.class));
    }

    @Test
    @DisplayName("Delete environment standard API success: return a successful response")
    void deleteEnvironmentStandard_success() throws Exception {
        EnvironmentStandardUpdateResponse response = EnvironmentStandardUpdateResponse.builder()
            .environmentStandardId(3001L)
            .environmentType(EnvironmentType.DRYROOM)
            .environmentCode("ENV-001")
            .environmentName("Dry Room")
            .build();

        when(environmentStandardManageCommandService.deleteEnvironmentStandard(3001L)).thenReturn(response);

        mockMvc.perform(delete(BASE_URL + "/environment-standards/3001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.environmentStandardId").value(3001L));

        verify(environmentStandardManageCommandService).deleteEnvironmentStandard(3001L);
    }

    @Test
    @DisplayName("Create environment event API success: return a successful response")
    void createEnvironmentEvent_success() throws Exception {
        EnvironmentEventCreateRequest request = createEnvironmentEventCreateRequest();

        EnvironmentEventCreateResponse response = EnvironmentEventCreateResponse.builder()
            .environmentEventId(5001L)
            .equipmentId(4001L)
            .envDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION)
            .envCorrectionApplied(false)
            .build();

        when(environmentEventManageCommandService.createEnvironmentEvent(any(EnvironmentEventCreateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/environment-events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.environmentEventId").value(5001L))
            .andExpect(jsonPath("$.data.equipmentId").value(4001L))
            .andExpect(jsonPath("$.data.envDeviationType").value("TEMPERATURE_DEVIATION"))
            .andExpect(jsonPath("$.data.envCorrectionApplied").value(false));

        verify(environmentEventManageCommandService).createEnvironmentEvent(any(EnvironmentEventCreateRequest.class));
    }

    @Test
    @DisplayName("Update environment event API success: return a successful response")
    void updateEnvironmentEvent_success() throws Exception {
        EnvironmentEventUpdateRequest request = createEnvironmentEventUpdateRequest();

        EnvironmentEventUpdateResponse response = EnvironmentEventUpdateResponse.builder()
            .environmentEventId(5001L)
            .equipmentId(4001L)
            .envDeviationType(EnvDeviationType.HUMIDITY_DEVIATION)
            .envCorrectionApplied(true)
            .build();

        when(environmentEventManageCommandService.updateEnvironmentEvent(eq(5001L), any(EnvironmentEventUpdateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(put(BASE_URL + "/environment-events/5001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.environmentEventId").value(5001L))
            .andExpect(jsonPath("$.data.envDeviationType").value("HUMIDITY_DEVIATION"))
            .andExpect(jsonPath("$.data.envCorrectionApplied").value(true));

        verify(environmentEventManageCommandService).updateEnvironmentEvent(eq(5001L), any(EnvironmentEventUpdateRequest.class));
    }

    @Test
    @DisplayName("Delete environment event API success: return a successful response")
    void deleteEnvironmentEvent_success() throws Exception {
        EnvironmentEventUpdateResponse response = EnvironmentEventUpdateResponse.builder()
            .environmentEventId(5001L)
            .equipmentId(4001L)
            .envDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION)
            .envCorrectionApplied(false)
            .build();

        when(environmentEventManageCommandService.deleteEnvironmentEvent(5001L)).thenReturn(response);

        mockMvc.perform(delete(BASE_URL + "/environment-events/5001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.environmentEventId").value(5001L));

        verify(environmentEventManageCommandService).deleteEnvironmentEvent(5001L);
    }

    @Test
    @DisplayName("Create equipment API success: return a successful response")
    void createEquipment_success() throws Exception {
        EquipmentCreateRequest request = createEquipmentRequest();

        EquipmentCreateResponse response = EquipmentCreateResponse.builder()
            .equipmentId(4001L)
            .equipmentCode("EQ-001")
            .equipmentName("Printing Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.S)
            .build();

        when(equipmentManageCommandService.createEquipment(any(EquipmentCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/equipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentId").value(4001L))
            .andExpect(jsonPath("$.data.equipmentCode").value("EQ-001"))
            .andExpect(jsonPath("$.data.equipmentName").value("Printing Equipment"))
            .andExpect(jsonPath("$.data.equipmentStatus").value("OPERATING"))
            .andExpect(jsonPath("$.data.equipmentGrade").value("S"));

        verify(equipmentManageCommandService).createEquipment(any(EquipmentCreateRequest.class));
    }

    @Test
    @DisplayName("Create equipment API failure: return 400 for invalid JSON")
    void createEquipment_whenInvalidRequest_thenBadRequest() throws Exception {
        String invalidJson = "{\"equipmentProcessId\":2001,\"equipmentCode\":";

        mockMvc.perform(post(BASE_URL + "/equipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update equipment API success: return a successful response")
    void updateEquipment_success() throws Exception {
        EquipmentUpdateRequest request = createEquipmentUpdateRequest();

        doNothing().when(equipmentManageCommandService).updateEquipment(eq(4001L), any(EquipmentUpdateRequest.class));

        mockMvc.perform(put(BASE_URL + "/equipments/4001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").doesNotExist());

        verify(equipmentManageCommandService).updateEquipment(eq(4001L), any(EquipmentUpdateRequest.class));
    }

    @Test
    @DisplayName("Update equipment API failure: propagate service exception")
    void updateEquipment_whenServiceThrows_thenPropagatesServletException() throws Exception {
        EquipmentUpdateRequest request = createEquipmentUpdateRequest();

        doThrow(new IllegalArgumentException("Equipment not found."))
            .when(equipmentManageCommandService).updateEquipment(eq(4001L), any(EquipmentUpdateRequest.class));

        ServletException exception = assertThrows(ServletException.class,
            () -> mockMvc.perform(put(BASE_URL + "/equipments/4001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))));

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Equipment not found.", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Delete equipment API success: return a successful response")
    void deleteEquipment_success() throws Exception {
        doNothing().when(equipmentManageCommandService).deleteEquipment(4001L);

        mockMvc.perform(delete(BASE_URL + "/equipments/4001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").doesNotExist());

        verify(equipmentManageCommandService).deleteEquipment(4001L);
    }

    private EnvironmentStandardCreateRequest createEnvironmentStandardCreateRequest() {
        return EnvironmentStandardCreateRequest.builder()
            .environmentType(EnvironmentType.DRYROOM)
            .environmentCode("ENV-001")
            .environmentName("Dry Room")
            .envTempMin(new BigDecimal("18.0"))
            .envTempMax(new BigDecimal("25.0"))
            .envHumidityMin(new BigDecimal("30.0"))
            .envHumidityMax(new BigDecimal("40.0"))
            .envParticleLimit(100)
            .build();
    }

    private EnvironmentStandardUpdateRequest createEnvironmentStandardUpdateRequest() {
        return EnvironmentStandardUpdateRequest.builder()
            .environmentType(EnvironmentType.CLEANROOM)
            .environmentCode("ENV-999")
            .environmentName("Clean Room")
            .envTempMin(new BigDecimal("20.0"))
            .envTempMax(new BigDecimal("24.0"))
            .envHumidityMin(new BigDecimal("35.0"))
            .envHumidityMax(new BigDecimal("45.0"))
            .envParticleLimit(80)
            .build();
    }

    private EnvironmentEventCreateRequest createEnvironmentEventCreateRequest() {
        return EnvironmentEventCreateRequest.builder()
            .equipmentId(4001L)
            .envTemperature(new BigDecimal("24.0"))
            .envHumidity(new BigDecimal("38.0"))
            .envParticleCnt(90)
            .envDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION)
            .envCorrectionApplied(false)
            .envDetectedAt(LocalDateTime.of(2026, 4, 1, 9, 0))
            .build();
    }

    private EnvironmentEventUpdateRequest createEnvironmentEventUpdateRequest() {
        return EnvironmentEventUpdateRequest.builder()
            .equipmentId(4001L)
            .envTemperature(new BigDecimal("26.0"))
            .envHumidity(new BigDecimal("42.0"))
            .envParticleCnt(110)
            .envDeviationType(EnvDeviationType.HUMIDITY_DEVIATION)
            .envCorrectionApplied(true)
            .envDetectedAt(LocalDateTime.of(2026, 4, 1, 10, 0))
            .build();
    }

    private EquipmentCreateRequest createEquipmentRequest() {
        return EquipmentCreateRequest.builder()
            .equipmentProcessId(2001L)
            .environmentStandardId(3001L)
            .equipmentCode("EQ-001")
            .equipmentName("Printing Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.S)
            .equipmentDescription("New equipment")
            .equipmentWarrantyMonth(24)
            .equipmentDesignLifeMonths(120)
            .equipmentWearCoefficient(0.75)
            .build();
    }

    private EquipmentUpdateRequest createEquipmentUpdateRequest() {
        return EquipmentUpdateRequest.builder()
            .equipmentProcessId(2001L)
            .environmentStandardId(3001L)
            .equipmentCode("EQ-002")
            .equipmentName("Updated Equipment")
            .equipmentStatus(EquipmentStatus.STOPPED)
            .equipmentGrade(EquipmentGrade.B)
            .equipmentDescription("Updated description")
            .equipmentWarrantyMonth(60)
            .equipmentDesignLifeMonths(120)
            .equipmentWearCoefficient(0.9)
            .build();
    }
}