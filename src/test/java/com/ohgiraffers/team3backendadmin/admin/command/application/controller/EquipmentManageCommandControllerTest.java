package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentEventCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentEventUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentStandardCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentStandardUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceItemStandardCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceItemStandardUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceLogCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceLogUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentEventCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentEventUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentStandardCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentStandardUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceItemStandardCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceItemStandardUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceLogCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceLogUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentProcessManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EnvironmentEventManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EnvironmentStandardManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.FactoryLineManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.MaintenanceItemStandardManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.MaintenanceLogManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
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

    @MockitoBean
    private MaintenanceItemStandardManageCommandService maintenanceItemStandardManageCommandService;

    @MockitoBean
    private MaintenanceLogManageCommandService maintenanceLogManageCommandService;

    @Test
    @DisplayName("Create factory line API success: return a successful response")
    void createFactoryLine_success() throws Exception {
        FactoryLineCreateRequest request = FactoryLineCreateRequest.builder()
            .factoryLineCode("LINE-001")
            .factoryLineName("Main Line")
            .build();

        FactoryLineCreateResponse response = FactoryLineCreateResponse.builder()
            .factoryLineId(1001L)
            .factoryLineCode("LINE-001")
            .factoryLineName("Main Line")
            .build();

        when(factoryLineManageCommandService.createFactoryLine(any(FactoryLineCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/factory-lines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineId").value(1001L))
            .andExpect(jsonPath("$.data.factoryLineCode").value("LINE-001"));
    }

    @Test
    @DisplayName("Update factory line API success: return a successful response")
    void updateFactoryLine_success() throws Exception {
        FactoryLineUpdateRequest request = FactoryLineUpdateRequest.builder()
            .factoryLineCode("LINE-999")
            .factoryLineName("Updated Line")
            .build();

        FactoryLineUpdateResponse response = FactoryLineUpdateResponse.builder()
            .factoryLineId(1001L)
            .factoryLineCode("LINE-999")
            .factoryLineName("Updated Line")
            .build();

        when(factoryLineManageCommandService.updateFactoryLine(eq(1001L), any(FactoryLineUpdateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(put(BASE_URL + "/factory-lines/1001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineName").value("Updated Line"));
    }

    @Test
    @DisplayName("Delete factory line API success: return a successful response")
    void deleteFactoryLine_success() throws Exception {
        FactoryLineUpdateResponse response = FactoryLineUpdateResponse.builder()
            .factoryLineId(1001L)
            .factoryLineCode("LINE-001")
            .factoryLineName("Main Line")
            .build();

        when(factoryLineManageCommandService.deleteFactoryLine(1001L)).thenReturn(response);

        mockMvc.perform(delete(BASE_URL + "/factory-lines/1001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineId").value(1001L));
    }

    @Test
    @DisplayName("Create equipment process API success: return a successful response")
    void createEquipmentProcess_success() throws Exception {
        EquipmentProcessCreateRequest request = EquipmentProcessCreateRequest.builder()
            .factoryLineId(1001L)
            .equipmentProcessCode("PROC-001")
            .equipmentProcessName("Mixing Process")
            .build();

        EquipmentProcessCreateResponse response = EquipmentProcessCreateResponse.builder()
            .equipmentProcessId(2001L)
            .factoryLineId(1001L)
            .equipmentProcessCode("PROC-001")
            .equipmentProcessName("Mixing Process")
            .build();

        when(equipmentProcessManageCommandService.createEquipmentProcess(any(EquipmentProcessCreateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/equipment-processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessId").value(2001L));
    }

    @Test
    @DisplayName("Update equipment process API success: return a successful response")
    void updateEquipmentProcess_success() throws Exception {
        EquipmentProcessUpdateRequest request = EquipmentProcessUpdateRequest.builder()
            .factoryLineId(1002L)
            .equipmentProcessCode("PROC-999")
            .equipmentProcessName("Updated Process")
            .build();

        EquipmentProcessUpdateResponse response = EquipmentProcessUpdateResponse.builder()
            .equipmentProcessId(2001L)
            .factoryLineId(1002L)
            .equipmentProcessCode("PROC-999")
            .equipmentProcessName("Updated Process")
            .build();

        when(equipmentProcessManageCommandService.updateEquipmentProcess(eq(2001L), any(EquipmentProcessUpdateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(put(BASE_URL + "/equipment-processes/2001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessName").value("Updated Process"));
    }

    @Test
    @DisplayName("Delete equipment process API success: return a successful response")
    void deleteEquipmentProcess_success() throws Exception {
        EquipmentProcessUpdateResponse response = EquipmentProcessUpdateResponse.builder()
            .equipmentProcessId(2001L)
            .factoryLineId(1001L)
            .equipmentProcessCode("PROC-001")
            .equipmentProcessName("Mixing Process")
            .build();

        when(equipmentProcessManageCommandService.deleteEquipmentProcess(2001L)).thenReturn(response);

        mockMvc.perform(delete(BASE_URL + "/equipment-processes/2001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessId").value(2001L));
    }

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
            .andExpect(jsonPath("$.data.environmentType").value("DRYROOM"));
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
            .andExpect(jsonPath("$.data.environmentStandardId").value(3001L));
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
            .andExpect(jsonPath("$.data.environmentEventId").value(5001L));
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
            .andExpect(jsonPath("$.data.environmentEventId").value(5001L));
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
    }

    @Test
    @DisplayName("Create maintenance item standard API success: return a successful response")
    void createMaintenanceItemStandard_success() throws Exception {
        MaintenanceItemStandardCreateRequest request = MaintenanceItemStandardCreateRequest.builder()
            .maintenanceItem("Bearing Check")
            .maintenanceWeight(new BigDecimal("0.30"))
            .maintenanceScoreMax(new BigDecimal("100.00"))
            .build();

        MaintenanceItemStandardCreateResponse response = MaintenanceItemStandardCreateResponse.builder()
            .maintenanceItemStandardId(6001L)
            .maintenanceItem("Bearing Check")
            .maintenanceWeight(new BigDecimal("0.30"))
            .maintenanceScoreMax(new BigDecimal("100.00"))
            .build();

        when(maintenanceItemStandardManageCommandService.createMaintenanceItemStandard(any(MaintenanceItemStandardCreateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/maintenance-item-standards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.maintenanceItemStandardId").value(6001L));
    }

    @Test
    @DisplayName("Update maintenance item standard API success: return a successful response")
    void updateMaintenanceItemStandard_success() throws Exception {
        MaintenanceItemStandardUpdateRequest request = MaintenanceItemStandardUpdateRequest.builder()
            .maintenanceItem("Sensor Check")
            .maintenanceWeight(new BigDecimal("0.45"))
            .maintenanceScoreMax(new BigDecimal("120.00"))
            .build();

        MaintenanceItemStandardUpdateResponse response = MaintenanceItemStandardUpdateResponse.builder()
            .maintenanceItemStandardId(6001L)
            .maintenanceItem("Sensor Check")
            .maintenanceWeight(new BigDecimal("0.45"))
            .maintenanceScoreMax(new BigDecimal("120.00"))
            .build();

        when(maintenanceItemStandardManageCommandService.updateMaintenanceItemStandard(eq(6001L), any(MaintenanceItemStandardUpdateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(put(BASE_URL + "/maintenance-item-standards/6001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.maintenanceItemStandardId").value(6001L));
    }

    @Test
    @DisplayName("Delete maintenance item standard API success: return a successful response")
    void deleteMaintenanceItemStandard_success() throws Exception {
        MaintenanceItemStandardUpdateResponse response = MaintenanceItemStandardUpdateResponse.builder()
            .maintenanceItemStandardId(6001L)
            .maintenanceItem("Bearing Check")
            .maintenanceWeight(new BigDecimal("0.30"))
            .maintenanceScoreMax(new BigDecimal("100.00"))
            .build();

        when(maintenanceItemStandardManageCommandService.deleteMaintenanceItemStandard(6001L)).thenReturn(response);

        mockMvc.perform(delete(BASE_URL + "/maintenance-item-standards/6001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.maintenanceItemStandardId").value(6001L));
    }

    @Test
    @DisplayName("Create maintenance log API success: return a successful response")
    void createMaintenanceLog_success() throws Exception {
        MaintenanceLogCreateRequest request = MaintenanceLogCreateRequest.builder()
            .equipmentId(4001L)
            .maintenanceItemStandardId(6001L)
            .maintenanceType(MaintenanceType.REGULAR)
            .maintenanceDate(java.time.LocalDate.of(2026, 4, 1))
            .maintenanceScore(new BigDecimal("91.50"))
            .etaMaintDelta(new BigDecimal("3.50"))
            .maintenanceResult(MaintenanceResult.NORMAL)
            .build();

        MaintenanceLogCreateResponse response = MaintenanceLogCreateResponse.builder()
            .maintenanceLogId(7001L)
            .equipmentId(4001L)
            .maintenanceItemStandardId(6001L)
            .maintenanceType(MaintenanceType.REGULAR)
            .maintenanceResult(MaintenanceResult.NORMAL)
            .build();

        when(maintenanceLogManageCommandService.createMaintenanceLog(any(MaintenanceLogCreateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/maintenance-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.maintenanceLogId").value(7001L));
    }

    @Test
    @DisplayName("Update maintenance log API success: return a successful response")
    void updateMaintenanceLog_success() throws Exception {
        MaintenanceLogUpdateRequest request = MaintenanceLogUpdateRequest.builder()
            .equipmentId(4001L)
            .maintenanceItemStandardId(6001L)
            .maintenanceType(MaintenanceType.IRREGULAR)
            .maintenanceDate(java.time.LocalDate.of(2026, 4, 2))
            .maintenanceScore(new BigDecimal("82.00"))
            .etaMaintDelta(new BigDecimal("5.00"))
            .maintenanceResult(MaintenanceResult.REPAIR_REQUIRED)
            .build();

        MaintenanceLogUpdateResponse response = MaintenanceLogUpdateResponse.builder()
            .maintenanceLogId(7001L)
            .equipmentId(4001L)
            .maintenanceItemStandardId(6001L)
            .maintenanceType(MaintenanceType.IRREGULAR)
            .maintenanceResult(MaintenanceResult.REPAIR_REQUIRED)
            .build();

        when(maintenanceLogManageCommandService.updateMaintenanceLog(eq(7001L), any(MaintenanceLogUpdateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(put(BASE_URL + "/maintenance-logs/7001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.maintenanceLogId").value(7001L));
    }

    @Test
    @DisplayName("Delete maintenance log API success: return a successful response")
    void deleteMaintenanceLog_success() throws Exception {
        MaintenanceLogUpdateResponse response = MaintenanceLogUpdateResponse.builder()
            .maintenanceLogId(7001L)
            .equipmentId(4001L)
            .maintenanceItemStandardId(6001L)
            .maintenanceType(MaintenanceType.REGULAR)
            .maintenanceResult(MaintenanceResult.NORMAL)
            .build();

        when(maintenanceLogManageCommandService.deleteMaintenanceLog(7001L)).thenReturn(response);

        mockMvc.perform(delete(BASE_URL + "/maintenance-logs/7001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.maintenanceLogId").value(7001L));
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
            .andExpect(jsonPath("$.data.equipmentId").value(4001L));
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
    }

    @Test
    @DisplayName("Update equipment API failure: return 404 when the target does not exist")
    void updateEquipment_whenServiceThrows_thenReturn404() throws Exception {
        EquipmentUpdateRequest request = createEquipmentUpdateRequest();

        doThrow(new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND))
            .when(equipmentManageCommandService).updateEquipment(eq(4001L), any(EquipmentUpdateRequest.class));

        mockMvc.perform(put(BASE_URL + "/equipments/4001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_006"));
    }

    @Test
    @DisplayName("Delete equipment API success: return a successful response")
    void deleteEquipment_success() throws Exception {
        doNothing().when(equipmentManageCommandService).deleteEquipment(4001L);

        mockMvc.perform(delete(BASE_URL + "/equipments/4001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").doesNotExist());
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
