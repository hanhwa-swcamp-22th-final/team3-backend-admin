package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvDeviationType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentEvent;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentEventRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentStandardRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentProcessRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.FactoryLineRepository;
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
    private EnvironmentEventRepository environmentEventRepository;

    @Autowired
    private EntityManager entityManager;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private FactoryLine factoryLine;
    private EquipmentProcess equipmentProcess;
    private EnvironmentStandard environmentStandard;
    private Equipment equipment;
    private EnvironmentEvent environmentEvent;

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
}
