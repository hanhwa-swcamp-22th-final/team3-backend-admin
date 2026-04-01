package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentEvent;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceItemStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceLog;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentAgingParam;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentBaseline;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentEventRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentStandardRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentAgingParamRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentBaselineRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentProcessRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.FactoryLineRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.MaintenanceItemStandardRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.MaintenanceLogRepository;
import com.ohgiraffers.team3backendadmin.common.idgenerator.TimeBasedIdGenerator;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class EquipmentManageQueryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FactoryLineRepository factoryLineRepository;

    @Autowired
    private EquipmentProcessRepository equipmentProcessRepository;

    @Autowired
    private EnvironmentStandardRepository environmentStandardRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentAgingParamRepository equipmentAgingParamRepository;

    @Autowired
    private EquipmentBaselineRepository equipmentBaselineRepository;

    @Autowired
    private EnvironmentEventRepository environmentEventRepository;

    @Autowired
    private MaintenanceItemStandardRepository maintenanceItemStandardRepository;

    @Autowired
    private MaintenanceLogRepository maintenanceLogRepository;

    @Autowired
    private EntityManager entityManager;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private FactoryLine factoryLine;
    private EquipmentProcess equipmentProcess;
    private EnvironmentStandard environmentStandard;
    private Equipment equipment;
    private EnvironmentEvent environmentEvent;
    private MaintenanceItemStandard maintenanceItemStandard;
    private MaintenanceLog maintenanceLog;
    private EquipmentAgingParam equipmentAgingParam;
    private EquipmentAgingParam latestEquipmentAgingParam;
    private EquipmentBaseline equipmentBaseline;
    private EquipmentBaseline latestEquipmentBaseline;

    @BeforeEach
    void setUp() {
        factoryLine = factoryLineRepository.save(
            new FactoryLine(
                idGenerator.generate(),
                "LINE-" + idGenerator.generate(),
                "Query Line"
            )
        );

        equipmentProcess = equipmentProcessRepository.save(
            new EquipmentProcess(
                idGenerator.generate(),
                factoryLine.getFactoryLineId(),
                "PROC-" + idGenerator.generate(),
                "Query Process"
            )
        );

        environmentStandard = environmentStandardRepository.save(
            EnvironmentStandard.builder()
                .environmentStandardId(idGenerator.generate())
                .environmentType(EnvironmentType.DRYROOM)
                .environmentCode("ENV-" + idGenerator.generate())
                .environmentName("Query Standard")
                .envTempMin(BigDecimal.valueOf(20.0))
                .envTempMax(BigDecimal.valueOf(25.0))
                .envHumidityMin(BigDecimal.valueOf(30.0))
                .envHumidityMax(BigDecimal.valueOf(40.0))
                .envParticleLimit(1000)
                .build()
        );

        equipment = equipmentRepository.save(
            Equipment.builder()
                .equipmentId(idGenerator.generate())
                .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
                .environmentStandardId(environmentStandard.getEnvironmentStandardId())
                .equipmentCode("EQ-" + idGenerator.generate())
                .equipmentName("Query Equipment")
                .equipmentStatus(EquipmentStatus.OPERATING)
                .equipmentGrade(EquipmentGrade.S)
                .equipmentDescription("Query integration equipment")
                .build()
        );

        equipmentAgingParam = equipmentAgingParamRepository.save(
            EquipmentAgingParam.builder()
                .equipmentAgingParamId(idGenerator.generate())
                .equipmentId(equipment.getEquipmentId())
                .equipmentWarrantyMonth(24)
                .equipmentDesignLifeMonths(120)
                .equipmentWearCoefficient(new BigDecimal("0.70"))
                .equipmentEtaAge(new BigDecimal("48.0"))
                .equipmentAgeMonths(12)
                .equipmentAgeCalculatedAt(LocalDateTime.of(2026, 3, 31, 9, 0))
                .build()
        );

        latestEquipmentAgingParam = equipmentAgingParamRepository.save(
            EquipmentAgingParam.builder()
                .equipmentAgingParamId(idGenerator.generate())
                .equipmentId(equipment.getEquipmentId())
                .equipmentWarrantyMonth(24)
                .equipmentDesignLifeMonths(120)
                .equipmentWearCoefficient(new BigDecimal("0.72"))
                .equipmentEtaAge(new BigDecimal("45.0"))
                .equipmentAgeMonths(13)
                .equipmentAgeCalculatedAt(LocalDateTime.of(2026, 4, 1, 9, 0))
                .build()
        );

        equipmentBaseline = equipmentBaselineRepository.save(
            EquipmentBaseline.builder()
                .equipmentBaselineId(idGenerator.generate())
                .equipmentId(equipment.getEquipmentId())
                .equipmentAgingParamId(equipmentAgingParam.getEquipmentAgingParamId())
                .equipmentStandardPerformanceRate(new BigDecimal("98.0"))
                .equipmentBaselineErrorRate(new BigDecimal("1.2"))
                .equipmentEtaMaint(new BigDecimal("120.0"))
                .equipmentIdx(new BigDecimal("91.0"))
                .equipmentBaselineCalculatedAt(LocalDateTime.of(2026, 3, 31, 10, 0))
                .build()
        );

        latestEquipmentBaseline = equipmentBaselineRepository.save(
            EquipmentBaseline.builder()
                .equipmentBaselineId(idGenerator.generate())
                .equipmentId(equipment.getEquipmentId())
                .equipmentAgingParamId(latestEquipmentAgingParam.getEquipmentAgingParamId())
                .equipmentStandardPerformanceRate(new BigDecimal("97.5"))
                .equipmentBaselineErrorRate(new BigDecimal("1.5"))
                .equipmentEtaMaint(new BigDecimal("110.0"))
                .equipmentIdx(new BigDecimal("89.5"))
                .equipmentBaselineCalculatedAt(LocalDateTime.of(2026, 4, 1, 10, 0))
                .build()
        );

        environmentEvent = environmentEventRepository.save(
            EnvironmentEvent.builder()
                .environmentEventId(idGenerator.generate())
                .equipmentId(equipment.getEquipmentId())
                .envTemperature(BigDecimal.valueOf(26.1))
                .envHumidity(BigDecimal.valueOf(45.0))
                .envParticleCnt(1200)
                .envDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION)
                .envCorrectionApplied(false)
                .envDetectedAt(LocalDateTime.of(2026, 4, 1, 11, 0))
                .build()
        );

        maintenanceItemStandard = maintenanceItemStandardRepository.save(
            MaintenanceItemStandard.builder()
                .maintenanceItemStandardId(idGenerator.generate())
                .maintenanceItem("Bearing Check")
                .maintenanceWeight(new BigDecimal("0.30"))
                .maintenanceScoreMax(new BigDecimal("100.00"))
                .build()
        );

        maintenanceLog = maintenanceLogRepository.save(
            MaintenanceLog.builder()
                .maintenanceLogId(idGenerator.generate())
                .equipmentId(equipment.getEquipmentId())
                .maintenanceItemStandardId(maintenanceItemStandard.getMaintenanceItemStandardId())
                .maintenanceType(MaintenanceType.REGULAR)
                .maintenanceDate(LocalDate.of(2026, 4, 1))
                .maintenanceScore(new BigDecimal("91.50"))
                .etaMaintDelta(new BigDecimal("3.50"))
                .maintenanceResult(MaintenanceResult.NORMAL)
                .build()
        );

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Get factory line list API integration success: return persisted factory line")
    void getFactoryLineList_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/factory-lines")
                .param("keyword", "Query Line"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].factoryLineId").value(factoryLine.getFactoryLineId()));
    }

    @Test
    @DisplayName("Get factory line detail API integration success: return persisted factory line detail")
    void getFactoryLineDetail_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/factory-lines/{factoryLineId}", factoryLine.getFactoryLineId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineId").value(factoryLine.getFactoryLineId()));
    }

    @Test
    @DisplayName("Get factory line detail API integration failure: return 404 when ID does not exist")
    void getFactoryLineDetail_whenNotFound_thenReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/factory-lines/{factoryLineId}", Long.MAX_VALUE))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_004"));
    }

    @Test
    @DisplayName("Get equipment process list API integration success: return persisted equipment process")
    void getEquipmentProcessList_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipment-processes")
                .param("factoryLineId", String.valueOf(factoryLine.getFactoryLineId()))
                .param("keyword", "Query Process"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].equipmentProcessId").value(equipmentProcess.getEquipmentProcessId()));
    }

    @Test
    @DisplayName("Get equipment process detail API integration success: return persisted equipment process detail")
    void getEquipmentProcessDetail_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipment-processes/{equipmentProcessId}", equipmentProcess.getEquipmentProcessId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessId").value(equipmentProcess.getEquipmentProcessId()));
    }

    @Test
    @DisplayName("Get equipment process detail API integration failure: return 404 when ID does not exist")
    void getEquipmentProcessDetail_whenNotFound_thenReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipment-processes/{equipmentProcessId}", Long.MAX_VALUE))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_005"));
    }

    @Test
    @DisplayName("Get environment standard list API integration success: return persisted standard")
    void getEnvironmentStandardList_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/environment-standards")
                .param("keyword", "Query Standard")
                .param("environmentType", "DRYROOM"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].environmentStandardId").value(environmentStandard.getEnvironmentStandardId()));
    }

    @Test
    @DisplayName("Get environment standard detail API integration success: return persisted standard detail")
    void getEnvironmentStandardDetail_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/environment-standards/{environmentStandardId}", environmentStandard.getEnvironmentStandardId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.environmentStandardId").value(environmentStandard.getEnvironmentStandardId()));
    }

    @Test
    @DisplayName("Get environment standard detail API integration failure: return 404 when ID does not exist")
    void getEnvironmentStandardDetail_whenNotFound_thenReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/environment-standards/{environmentStandardId}", Long.MAX_VALUE))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_007"));
    }

    @Test
    @DisplayName("Get environment event list API integration success: return persisted event")
    void getEnvironmentEventList_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/environment-events")
                .param("equipmentId", String.valueOf(equipment.getEquipmentId()))
                .param("envDeviationType", "TEMPERATURE_DEVIATION"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].environmentEventId").value(environmentEvent.getEnvironmentEventId()));
    }

    @Test
    @DisplayName("Get environment event detail API integration success: return persisted event detail")
    void getEnvironmentEventDetail_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/environment-events/{environmentEventId}", environmentEvent.getEnvironmentEventId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.environmentEventId").value(environmentEvent.getEnvironmentEventId()));
    }

    @Test
    @DisplayName("Get environment event detail API integration failure: return 404 when ID does not exist")
    void getEnvironmentEventDetail_whenNotFound_thenReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/environment-events/{environmentEventId}", Long.MAX_VALUE))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_008"));
    }

    @Test
    @DisplayName("Get latest environment event API integration success")
    void getLatestEnvironmentEvent_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/environment-events")
                .param("mode", "latest")
                .param("equipmentId", String.valueOf(equipment.getEquipmentId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.environmentEventId").value(environmentEvent.getEnvironmentEventId()));
    }

    @Test
    @DisplayName("Get maintenance item standard list API integration success: return persisted item standard")
    void getMaintenanceItemStandardList_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/maintenance-item-standards")
                .param("keyword", "Bearing Check"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].maintenanceItemStandardId").value(maintenanceItemStandard.getMaintenanceItemStandardId()));
    }

    @Test
    @DisplayName("Get maintenance item standard detail API integration success: return persisted item standard detail")
    void getMaintenanceItemStandardDetail_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/maintenance-item-standards/{maintenanceItemStandardId}",
                maintenanceItemStandard.getMaintenanceItemStandardId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.maintenanceItemStandardId").value(maintenanceItemStandard.getMaintenanceItemStandardId()));
    }

    @Test
    @DisplayName("Get maintenance item standard detail API integration failure: return 404 when ID does not exist")
    void getMaintenanceItemStandardDetail_whenNotFound_thenReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/maintenance-item-standards/{maintenanceItemStandardId}", Long.MAX_VALUE))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_010"));
    }

    @Test
    @DisplayName("Get maintenance item standard detail API integration failure: return 404 when item standard is soft deleted")
    void getMaintenanceItemStandardDetail_whenSoftDeleted_thenReturn404() throws Exception {
        maintenanceItemStandard.softDelete();
        maintenanceItemStandardRepository.save(maintenanceItemStandard);
        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(get("/api/v1/equipment-management/maintenance-item-standards/{maintenanceItemStandardId}",
                maintenanceItemStandard.getMaintenanceItemStandardId()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_010"));
    }

    @Test
    @DisplayName("Get maintenance log list API integration success: return persisted maintenance log")
    void getMaintenanceLogList_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/maintenance-logs")
                .param("equipmentId", String.valueOf(equipment.getEquipmentId()))
                .param("maintenanceType", "REGULAR")
                .param("maintenanceResult", "NORMAL"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].maintenanceLogId").value(maintenanceLog.getMaintenanceLogId()));
    }

    @Test
    @DisplayName("Get maintenance log detail API integration success: return persisted maintenance log detail")
    void getMaintenanceLogDetail_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/maintenance-logs/{maintenanceLogId}", maintenanceLog.getMaintenanceLogId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.maintenanceLogId").value(maintenanceLog.getMaintenanceLogId()));
    }

    @Test
    @DisplayName("Get maintenance log detail API integration failure: return 404 when ID does not exist")
    void getMaintenanceLogDetail_whenNotFound_thenReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/maintenance-logs/{maintenanceLogId}", Long.MAX_VALUE))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_011"));
    }

    @Test
    @DisplayName("Get latest maintenance log API integration success")
    void getLatestMaintenanceLog_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/maintenance-logs")
                .param("mode", "latest")
                .param("equipmentId", String.valueOf(equipment.getEquipmentId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.maintenanceLogId").value(maintenanceLog.getMaintenanceLogId()));
    }

    @Test
    @DisplayName("Get equipment list API integration success: return persisted equipment")
    void getEquipmentList_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipments")
                .param("keyword", "Query Equipment"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].equipmentId").value(equipment.getEquipmentId()));
    }

    @Test
    @DisplayName("Get equipment detail API integration success: return persisted equipment detail")
    void getEquipmentDetail_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipments/{equipmentId}", equipment.getEquipmentId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentId").value(equipment.getEquipmentId()));
    }

    @Test
    @DisplayName("Get equipment detail API integration failure: return 404 when ID does not exist")
    void getEquipmentDetail_whenNotFound_thenReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipments/{equipmentId}", Long.MAX_VALUE))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_006"));
    }

    @Test
    @DisplayName("Get equipment list with latest snapshots API integration success")
    void getEquipmentListWithLatestSnapshots_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipments")
                .param("mode", "latest-snapshots")
                .param("keyword", "Query Equipment"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].equipmentId").value(equipment.getEquipmentId()))
            .andExpect(jsonPath("$.data[0].latestEquipmentAgingParamId").value(latestEquipmentAgingParam.getEquipmentAgingParamId()))
            .andExpect(jsonPath("$.data[0].latestEquipmentBaselineId").value(latestEquipmentBaseline.getEquipmentBaselineId()));
    }

    @Test
    @DisplayName("Get equipment aging param history API integration success")
    void getEquipmentAgingParamHistory_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipment-aging-params")
                .param("equipmentId", String.valueOf(equipment.getEquipmentId()))
                .param("calculatedFrom", "2026-03-31T00:00:00")
                .param("calculatedTo", "2026-04-01T23:59:59"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].equipmentAgingParamId").exists());
    }

    @Test
    @DisplayName("Get latest equipment aging param API integration success")
    void getLatestEquipmentAgingParam_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipment-aging-params")
                .param("mode", "latest")
                .param("equipmentId", String.valueOf(equipment.getEquipmentId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.equipmentAgingParamId").value(latestEquipmentAgingParam.getEquipmentAgingParamId()));
    }

    @Test
    @DisplayName("Get equipment baseline history API integration success")
    void getEquipmentBaselineHistory_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipment-baselines")
                .param("equipmentId", String.valueOf(equipment.getEquipmentId()))
                .param("calculatedFrom", "2026-03-31T00:00:00")
                .param("calculatedTo", "2026-04-01T23:59:59"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].equipmentBaselineId").exists());
    }

    @Test
    @DisplayName("Get latest equipment baseline API integration success")
    void getLatestEquipmentBaseline_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipment-baselines")
                .param("mode", "latest")
                .param("equipmentId", String.valueOf(equipment.getEquipmentId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.equipmentBaselineId").value(latestEquipmentBaseline.getEquipmentBaselineId()));
    }

}
