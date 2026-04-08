package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentEventDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentEventQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentAgingParamDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentBaselineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentLatestSnapshotQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentProcessDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentProcessQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentSummaryQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.FactoryLineEquipmentStatsResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.FactoryLineQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.MaintenanceItemStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.MaintenanceItemStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.MaintenanceLogDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.MaintenanceLogQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EnvironmentEventQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EnvironmentStandardQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentAgingParamQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentBaselineQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentProcessQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.FactoryLineQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.MaintenanceItemStandardQueryService;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.MaintenanceLogQueryService;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

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
    private EquipmentAgingParamQueryService equipmentAgingParamQueryService;

    @MockitoBean
    private EquipmentBaselineQueryService equipmentBaselineQueryService;

    @MockitoBean
    private EnvironmentStandardQueryService environmentStandardQueryService;

    @MockitoBean
    private EnvironmentEventQueryService environmentEventQueryService;

    @MockitoBean
    private MaintenanceItemStandardQueryService maintenanceItemStandardQueryService;

    @MockitoBean
    private MaintenanceLogQueryService maintenanceLogQueryService;

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
    @DisplayName("Get factory line equipment stats API success")
    void getFactoryLineEquipmentStats_success() throws Exception {
        FactoryLineEquipmentStatsResponse response = new FactoryLineEquipmentStatsResponse();
        response.setTotalEquipmentCount(10L);
        response.setOperatingEquipmentCount(7L);
        response.setOperationRate(new BigDecimal("70.00"));

        when(factoryLineQueryService.getFactoryLineEquipmentStats(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/factory-lines/1/equipment-stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.totalEquipmentCount").value(10))
            .andExpect(jsonPath("$.data.operatingEquipmentCount").value(7))
            .andExpect(jsonPath("$.data.operationRate").value(70.00));
    }

    @Test
    @DisplayName("Get factory line detail API failure: return 404 when the target does not exist")
    void getFactoryLineDetail_whenServiceThrows_thenReturn404() throws Exception {
        when(factoryLineQueryService.getFactoryLineDetail(999L))
            .thenThrow(new BusinessException(ErrorCode.FACTORY_LINE_NOT_FOUND));

        mockMvc.perform(get("/api/v1/equipment-management/factory-lines/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_004"));
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
    @DisplayName("Get equipment process detail API failure: return 404 when the target does not exist")
    void getEquipmentProcessDetail_whenServiceThrows_thenReturn404() throws Exception {
        when(equipmentProcessQueryService.getEquipmentProcessDetail(999L))
            .thenThrow(new BusinessException(ErrorCode.EQUIPMENT_PROCESS_NOT_FOUND));

        mockMvc.perform(get("/api/v1/equipment-management/equipment-processes/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_005"));
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
    @DisplayName("Get environment standard detail failure: return 404 when the target does not exist")
    void getEnvironmentStandardDetail_whenServiceThrows_thenReturn404() throws Exception {
        when(environmentStandardQueryService.getEnvironmentStandardDetail(999L))
            .thenThrow(new BusinessException(ErrorCode.ENVIRONMENT_STANDARD_NOT_FOUND));

        mockMvc.perform(get("/api/v1/equipment-management/environment-standards/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_007"));
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
    @DisplayName("Get latest environment event API success")
    void getLatestEnvironmentEvent_success() throws Exception {
        EnvironmentEventDetailResponse response = new EnvironmentEventDetailResponse();
        response.setEnvironmentEventId(11L);

        when(environmentEventQueryService.getLatestEnvironmentEvent(10L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/environment-events")
                .param("mode", "latest")
                .param("equipmentId", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.environmentEventId").value(11L));
    }

    @Test
    @DisplayName("Get environment event as-of API success")
    void getLatestEnvironmentEventAsOf_success() throws Exception {
        EnvironmentEventDetailResponse response = new EnvironmentEventDetailResponse();
        response.setEnvironmentEventId(12L);

        when(environmentEventQueryService.getLatestEnvironmentEventBeforeOrAt(
            10L, LocalDateTime.of(2026, 4, 1, 10, 0)))
            .thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/environment-events")
                .param("mode", "as-of")
                .param("equipmentId", "10")
                .param("referenceTime", "2026-04-01T10:00:00"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.environmentEventId").value(12L));
    }

    @Test
    @DisplayName("Get unresolved environment event list API success")
    void getUnresolvedEnvironmentEventList_success() throws Exception {
        EnvironmentEventQueryResponse response = new EnvironmentEventQueryResponse();
        response.setEnvironmentEventId(13L);

        when(environmentEventQueryService.getUnresolvedEnvironmentEventList()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/environment-events")
                .param("mode", "unresolved"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].environmentEventId").value(13L));
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
    @DisplayName("Get environment event detail failure: return 404 when the target does not exist")
    void getEnvironmentEventDetail_whenServiceThrows_thenReturn404() throws Exception {
        when(environmentEventQueryService.getEnvironmentEventDetail(999L))
            .thenThrow(new BusinessException(ErrorCode.ENVIRONMENT_EVENT_NOT_FOUND));

        mockMvc.perform(get("/api/v1/equipment-management/environment-events/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_008"));
    }

    @Test
    @DisplayName("Get maintenance item standard list API success: return list JSON")
    void getMaintenanceItemStandardList_success() throws Exception {
        MaintenanceItemStandardQueryResponse response = new MaintenanceItemStandardQueryResponse();
        response.setMaintenanceItemStandardId(1L);
        response.setMaintenanceItem("Bearing Check");
        response.setMaintenanceWeight(new java.math.BigDecimal("0.30"));
        response.setMaintenanceScoreMax(new java.math.BigDecimal("100.00"));

        when(maintenanceItemStandardQueryService.getMaintenanceItemStandardList(argThat(request -> request != null)))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/maintenance-item-standards"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].maintenanceItemStandardId").value(1L));
    }

    @Test
    @DisplayName("Get maintenance item standard list API success: bind query parameters correctly")
    void getMaintenanceItemStandardList_withQueryParams_success() throws Exception {
        when(maintenanceItemStandardQueryService.getMaintenanceItemStandardList(argThat(request -> request != null)))
            .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/equipment-management/maintenance-item-standards")
                .param("keyword", "bearing"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(maintenanceItemStandardQueryService)
            .getMaintenanceItemStandardList(argThat(request -> "bearing".equals(request.getKeyword())));
    }

    @Test
    @DisplayName("Get maintenance item standard detail API success: return detail JSON")
    void getMaintenanceItemStandardDetail_success() throws Exception {
        MaintenanceItemStandardDetailResponse response = new MaintenanceItemStandardDetailResponse();
        response.setMaintenanceItemStandardId(1L);
        response.setMaintenanceItem("Bearing Check");
        response.setMaintenanceWeight(new java.math.BigDecimal("0.30"));
        response.setMaintenanceScoreMax(new java.math.BigDecimal("100.00"));

        when(maintenanceItemStandardQueryService.getMaintenanceItemStandardDetail(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/maintenance-item-standards/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.maintenanceItemStandardId").value(1L));
    }

    @Test
    @DisplayName("Get maintenance item standard detail failure: return 404 when the target does not exist")
    void getMaintenanceItemStandardDetail_whenServiceThrows_thenReturn404() throws Exception {
        when(maintenanceItemStandardQueryService.getMaintenanceItemStandardDetail(999L))
            .thenThrow(new BusinessException(ErrorCode.MAINTENANCE_ITEM_STANDARD_NOT_FOUND));

        mockMvc.perform(get("/api/v1/equipment-management/maintenance-item-standards/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_010"));
    }

    @Test
    @DisplayName("Get maintenance log list API success: return list JSON")
    void getMaintenanceLogList_success() throws Exception {
        MaintenanceLogQueryResponse response = new MaintenanceLogQueryResponse();
        response.setMaintenanceLogId(1L);
        response.setEquipmentId(10L);
        response.setEquipmentCode("EQ-001");
        response.setEquipmentName("Drying Equipment");
        response.setMaintenanceItemStandardId(100L);
        response.setMaintenanceItem("Bearing Check");
        response.setMaintenanceType(MaintenanceType.REGULAR);
        response.setMaintenanceResult(MaintenanceResult.NORMAL);

        when(maintenanceLogQueryService.getMaintenanceLogList(argThat(request -> request != null)))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/maintenance-logs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].maintenanceLogId").value(1L));
    }

    @Test
    @DisplayName("Get maintenance log list API success: bind query parameters correctly")
    void getMaintenanceLogList_withQueryParams_success() throws Exception {
        when(maintenanceLogQueryService.getMaintenanceLogList(argThat(request -> request != null)))
            .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/equipment-management/maintenance-logs")
                .param("equipmentId", "10")
                .param("maintenanceType", "REGULAR")
                .param("maintenanceResult", "NORMAL"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(maintenanceLogQueryService).getMaintenanceLogList(argThat(request ->
            Long.valueOf(10L).equals(request.getEquipmentId())
                && MaintenanceType.REGULAR == request.getMaintenanceType()
                && MaintenanceResult.NORMAL == request.getMaintenanceResult()
        ));
    }

    @Test
    @DisplayName("Get latest maintenance log API success")
    void getLatestMaintenanceLog_success() throws Exception {
        MaintenanceLogDetailResponse response = new MaintenanceLogDetailResponse();
        response.setMaintenanceLogId(21L);

        when(maintenanceLogQueryService.getLatestMaintenanceLog(10L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/maintenance-logs")
                .param("mode", "latest")
                .param("equipmentId", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.maintenanceLogId").value(21L));
    }

    @Test
    @DisplayName("Get maintenance log as-of API success")
    void getLatestMaintenanceLogAsOf_success() throws Exception {
        MaintenanceLogDetailResponse response = new MaintenanceLogDetailResponse();
        response.setMaintenanceLogId(22L);

        when(maintenanceLogQueryService.getLatestMaintenanceLogBeforeOrAt(10L, LocalDate.of(2026, 4, 1)))
            .thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/maintenance-logs")
                .param("mode", "as-of")
                .param("equipmentId", "10")
                .param("referenceDate", "2026-04-01"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.maintenanceLogId").value(22L));
    }

    @Test
    @DisplayName("Get abnormal maintenance log list API success")
    void getAbnormalOrIncompleteMaintenanceLogs_success() throws Exception {
        MaintenanceLogQueryResponse response = new MaintenanceLogQueryResponse();
        response.setMaintenanceLogId(23L);

        when(maintenanceLogQueryService.getAbnormalOrIncompleteMaintenanceLogList()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/maintenance-logs")
                .param("mode", "anomalies"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].maintenanceLogId").value(23L));
    }

    @Test
    @DisplayName("Get maintenance log detail API success: return detail JSON")
    void getMaintenanceLogDetail_success() throws Exception {
        MaintenanceLogDetailResponse response = new MaintenanceLogDetailResponse();
        response.setMaintenanceLogId(1L);
        response.setEquipmentId(10L);
        response.setEquipmentCode("EQ-001");
        response.setEquipmentName("Drying Equipment");
        response.setMaintenanceItemStandardId(100L);
        response.setMaintenanceItem("Bearing Check");
        response.setMaintenanceType(MaintenanceType.REGULAR);
        response.setMaintenanceResult(MaintenanceResult.NORMAL);

        when(maintenanceLogQueryService.getMaintenanceLogDetail(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/maintenance-logs/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.maintenanceLogId").value(1L));
    }

    @Test
    @DisplayName("Get maintenance log detail failure: return 404 when the target does not exist")
    void getMaintenanceLogDetail_whenServiceThrows_thenReturn404() throws Exception {
        when(maintenanceLogQueryService.getMaintenanceLogDetail(999L))
            .thenThrow(new BusinessException(ErrorCode.MAINTENANCE_LOG_NOT_FOUND));

        mockMvc.perform(get("/api/v1/equipment-management/maintenance-logs/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_011"));
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
    @DisplayName("Get equipment summary API success")
    void getEquipmentSummary_success() throws Exception {
        EquipmentSummaryQueryResponse response = new EquipmentSummaryQueryResponse();
        response.setTotalCount(10L);
        response.setOperatingCount(4L);
        response.setStoppedCount(3L);
        response.setUnderInspectionCount(2L);
        response.setDisposedCount(1L);

        when(equipmentQueryService.getEquipmentSummary()).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/equipments")
                .param("mode", "summary"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.totalCount").value(10))
            .andExpect(jsonPath("$.data.operatingCount").value(4))
            .andExpect(jsonPath("$.data.stoppedCount").value(3))
            .andExpect(jsonPath("$.data.underInspectionCount").value(2))
            .andExpect(jsonPath("$.data.disposedCount").value(1));
    }

    @Test
    @DisplayName("Get equipment list with latest snapshots API success")
    void getEquipmentListWithLatestSnapshots_success() throws Exception {
        EquipmentLatestSnapshotQueryResponse response = new EquipmentLatestSnapshotQueryResponse();
        response.setEquipmentId(31L);
        response.setLatestEquipmentBaselineId(601L);

        when(equipmentQueryService.getEquipmentListWithLatestSnapshots(argThat(request -> request != null)))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/equipments")
                .param("mode", "latest-snapshots"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].equipmentId").value(31L))
            .andExpect(jsonPath("$.data[0].latestEquipmentBaselineId").value(601L));
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
    @DisplayName("Get equipment detail API failure: return 404 when the target does not exist")
    void getEquipmentDetail_whenServiceThrows_thenReturn404() throws Exception {
        when(equipmentQueryService.getEquipmentDetail(999L))
            .thenThrow(new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND));

        mockMvc.perform(get("/api/v1/equipment-management/equipments/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_006"));
    }

    @Test
    @DisplayName("Get equipment aging param detail API success: return detail JSON")
    void getEquipmentAgingParamDetail_success() throws Exception {
        EquipmentAgingParamDetailResponse response = new EquipmentAgingParamDetailResponse();
        response.setEquipmentAgingParamId(5001L);
        response.setEquipmentId(4001L);
        response.setEquipmentEtaAge(new java.math.BigDecimal("36.5"));
        response.setEquipmentWarrantyMonth(36);

        when(equipmentAgingParamQueryService.getEquipmentAgingParamDetail(5001L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/equipment-aging-params/5001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentAgingParamId").value(5001L));
    }

    @Test
    @DisplayName("Get equipment aging param detail failure: return 404 when the target does not exist")
    void getEquipmentAgingParamDetail_whenServiceThrows_thenReturn404() throws Exception {
        when(equipmentAgingParamQueryService.getEquipmentAgingParamDetail(999L))
            .thenThrow(new BusinessException(ErrorCode.EQUIPMENT_AGING_PARAM_NOT_FOUND));

        mockMvc.perform(get("/api/v1/equipment-management/equipment-aging-params/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_009"));
    }

    @Test
    @DisplayName("Get equipment aging param history API success")
    void getEquipmentAgingParamHistory_success() throws Exception {
        EquipmentAgingParamDetailResponse response = new EquipmentAgingParamDetailResponse();
        response.setEquipmentAgingParamId(5002L);

        when(equipmentAgingParamQueryService.getEquipmentAgingParamHistory(argThat(request -> request != null)))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/equipment-aging-params")
                .param("equipmentId", "4001")
                .param("calculatedFrom", "2026-04-01T00:00:00")
                .param("calculatedTo", "2026-04-02T00:00:00"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].equipmentAgingParamId").value(5002L));
    }

    @Test
    @DisplayName("Get latest equipment aging param API success")
    void getLatestEquipmentAgingParam_success() throws Exception {
        EquipmentAgingParamDetailResponse response = new EquipmentAgingParamDetailResponse();
        response.setEquipmentAgingParamId(5003L);

        when(equipmentAgingParamQueryService.getLatestEquipmentAgingParam(4001L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/equipment-aging-params")
                .param("mode", "latest")
                .param("equipmentId", "4001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.equipmentAgingParamId").value(5003L));
    }

    @Test
    @DisplayName("Get uncalculated equipment aging param list API success")
    void getUncalculatedEquipmentAgingParamList_success() throws Exception {
        EquipmentAgingParamDetailResponse response = new EquipmentAgingParamDetailResponse();
        response.setEquipmentAgingParamId(5004L);

        when(equipmentAgingParamQueryService.getUncalculatedEquipmentAgingParamList()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/equipment-aging-params")
                .param("mode", "uncalculated"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].equipmentAgingParamId").value(5004L));
    }

    @Test
    @DisplayName("Get equipment baseline detail API success: return detail JSON")
    void getEquipmentBaselineDetail_success() throws Exception {
        EquipmentBaselineDetailResponse response = new EquipmentBaselineDetailResponse();
        response.setEquipmentBaselineId(6001L);
        response.setEquipmentId(4001L);
        response.setEquipmentAgingParamId(5001L);
        response.setEquipmentStandardPerformanceRate(new java.math.BigDecimal("97.5"));

        when(equipmentBaselineQueryService.getEquipmentBaselineDetail(6001L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/equipment-baselines/6001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentBaselineId").value(6001L));
    }

    @Test
    @DisplayName("Get equipment baseline detail failure: return 404 when the target does not exist")
    void getEquipmentBaselineDetail_whenServiceThrows_thenReturn404() throws Exception {
        when(equipmentBaselineQueryService.getEquipmentBaselineDetail(999L))
            .thenThrow(new BusinessException(ErrorCode.EQUIPMENT_BASELINE_NOT_FOUND));

        mockMvc.perform(get("/api/v1/equipment-management/equipment-baselines/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_012"));
    }

    @Test
    @DisplayName("Get equipment baseline history API success")
    void getEquipmentBaselineHistory_success() throws Exception {
        EquipmentBaselineDetailResponse response = new EquipmentBaselineDetailResponse();
        response.setEquipmentBaselineId(6002L);

        when(equipmentBaselineQueryService.getEquipmentBaselineHistory(argThat(request -> request != null)))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/equipment-baselines")
                .param("equipmentId", "4001")
                .param("calculatedFrom", "2026-04-01T00:00:00")
                .param("calculatedTo", "2026-04-02T00:00:00"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].equipmentBaselineId").value(6002L));
    }

    @Test
    @DisplayName("Get latest equipment baseline API success")
    void getLatestEquipmentBaseline_success() throws Exception {
        EquipmentBaselineDetailResponse response = new EquipmentBaselineDetailResponse();
        response.setEquipmentBaselineId(6003L);

        when(equipmentBaselineQueryService.getLatestEquipmentBaseline(4001L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/equipment-management/equipment-baselines")
                .param("mode", "latest")
                .param("equipmentId", "4001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.equipmentBaselineId").value(6003L));
    }

    @Test
    @DisplayName("Get uncalculated equipment baseline list API success")
    void getUncalculatedEquipmentBaselineList_success() throws Exception {
        EquipmentBaselineDetailResponse response = new EquipmentBaselineDetailResponse();
        response.setEquipmentBaselineId(6004L);

        when(equipmentBaselineQueryService.getUncalculatedEquipmentBaselineList()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/equipment-management/equipment-baselines")
                .param("mode", "uncalculated"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].equipmentBaselineId").value(6004L));
    }

}
