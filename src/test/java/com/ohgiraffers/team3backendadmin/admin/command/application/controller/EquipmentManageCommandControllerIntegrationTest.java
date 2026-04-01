package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentEventCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentEventUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentStandardCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentStandardUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceItemStandardCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceItemStandardUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceLogCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceLogUpdateRequest;
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
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentQueryService;
import com.ohgiraffers.team3backendadmin.common.idgenerator.TimeBasedIdGenerator;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class EquipmentManageCommandControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FactoryLineRepository factoryLineRepository;

    @Autowired
    private EquipmentProcessRepository equipmentProcessRepository;

    @Autowired
    private EnvironmentStandardRepository environmentStandardRepository;

    @Autowired
    private EnvironmentEventRepository environmentEventRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentAgingParamRepository equipmentAgingParamRepository;

    @Autowired
    private EquipmentBaselineRepository equipmentBaselineRepository;

    @Autowired
    private MaintenanceItemStandardRepository maintenanceItemStandardRepository;

    @Autowired
    private MaintenanceLogRepository maintenanceLogRepository;

    @Autowired
    private EquipmentQueryService equipmentQueryService;

    @Autowired
    private EntityManager entityManager;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private FactoryLine factoryLine;
    private FactoryLine anotherFactoryLine;
    private EquipmentProcess equipmentProcess;
    private EnvironmentStandard environmentStandard;
    private Equipment equipment;
    private MaintenanceItemStandard maintenanceItemStandard;
    private MaintenanceLog maintenanceLog;

    @BeforeEach
    void setUp() {
        factoryLine = factoryLineRepository.save(
            new FactoryLine(
                idGenerator.generate(),
                "LINE-" + idGenerator.generate(),
                "Main Line"
            )
        );

        anotherFactoryLine = factoryLineRepository.save(
            new FactoryLine(
                idGenerator.generate(),
                "LINE-" + idGenerator.generate(),
                "Second Line"
            )
        );

        equipmentProcess = equipmentProcessRepository.save(
            new EquipmentProcess(
                idGenerator.generate(),
                factoryLine.getFactoryLineId(),
                "PROC-" + idGenerator.generate(),
                "Drying Process"
            )
        );

        environmentStandard = environmentStandardRepository.save(
            EnvironmentStandard.builder()
                .environmentStandardId(idGenerator.generate())
                .environmentType(EnvironmentType.DRYROOM)
                .environmentCode("ENV-" + idGenerator.generate())
                .environmentName("Dry Room")
                .envTempMin(BigDecimal.valueOf(20.0))
                .envTempMax(BigDecimal.valueOf(25.0))
                .envHumidityMin(BigDecimal.valueOf(30.0))
                .envHumidityMax(BigDecimal.valueOf(40.0))
                .envParticleLimit(1000)
                .build()
        );

        equipment = createEquipmentAggregate("EQ-" + idGenerator.generate(), "Seed Equipment");

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
    @DisplayName("Create factory line API integration success: persist factory line")
    void createFactoryLine_success() throws Exception {
        String factoryLineCode = "LINE-" + idGenerator.generate();

        FactoryLineCreateRequest request = FactoryLineCreateRequest.builder()
            .factoryLineCode(factoryLineCode)
            .factoryLineName("Integration Line")
            .build();

        mockMvc.perform(post("/api/v1/equipment-management/factory-lines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineCode").value(factoryLineCode));

        FactoryLine savedFactoryLine = factoryLineRepository.findByFactoryLineCode(factoryLineCode).orElse(null);
        assertNotNull(savedFactoryLine);
        assertEquals("Integration Line", savedFactoryLine.getFactoryLineName());
        assertFalse(savedFactoryLine.getIsDeleted());
    }

    @Test
    @DisplayName("Update factory line API integration success: update factory line fields")
    void updateFactoryLine_success() throws Exception {
        String updatedCode = "LINE-" + idGenerator.generate();

        FactoryLineUpdateRequest request = FactoryLineUpdateRequest.builder()
            .factoryLineCode(updatedCode)
            .factoryLineName("Updated Line")
            .build();

        mockMvc.perform(put("/api/v1/equipment-management/factory-lines/{factoryLineId}", factoryLine.getFactoryLineId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineCode").value(updatedCode));

        FactoryLine updatedFactoryLine = factoryLineRepository.findById(factoryLine.getFactoryLineId()).orElse(null);
        assertNotNull(updatedFactoryLine);
        assertEquals(updatedCode, updatedFactoryLine.getFactoryLineCode());
        assertEquals("Updated Line", updatedFactoryLine.getFactoryLineName());
    }

    @Test
    @DisplayName("Delete factory line API integration success: soft-delete target factory line")
    void deleteFactoryLine_success() throws Exception {
        mockMvc.perform(delete("/api/v1/equipment-management/factory-lines/{factoryLineId}", factoryLine.getFactoryLineId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        FactoryLine deletedFactoryLine = factoryLineRepository.findById(factoryLine.getFactoryLineId()).orElse(null);
        assertNotNull(deletedFactoryLine);
        assertTrue(deletedFactoryLine.getIsDeleted());
    }

    @Test
    @DisplayName("Create equipment process API integration success: persist equipment process")
    void createEquipmentProcess_success() throws Exception {
        String equipmentProcessCode = "PROC-" + idGenerator.generate();

        EquipmentProcessCreateRequest request = EquipmentProcessCreateRequest.builder()
            .factoryLineId(factoryLine.getFactoryLineId())
            .equipmentProcessCode(equipmentProcessCode)
            .equipmentProcessName("Integration Process")
            .build();

        mockMvc.perform(post("/api/v1/equipment-management/equipment-processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessCode").value(equipmentProcessCode));

        EquipmentProcess savedEquipmentProcess = equipmentProcessRepository.findByEquipmentProcessCode(equipmentProcessCode).orElse(null);
        assertNotNull(savedEquipmentProcess);
        assertEquals(factoryLine.getFactoryLineId(), savedEquipmentProcess.getFactoryLineId());
        assertFalse(savedEquipmentProcess.getIsDeleted());
    }

    @Test
    @DisplayName("Update equipment process API integration success: update process fields")
    void updateEquipmentProcess_success() throws Exception {
        String updatedCode = "PROC-" + idGenerator.generate();

        EquipmentProcessUpdateRequest request = EquipmentProcessUpdateRequest.builder()
            .factoryLineId(anotherFactoryLine.getFactoryLineId())
            .equipmentProcessCode(updatedCode)
            .equipmentProcessName("Updated Process")
            .build();

        mockMvc.perform(put("/api/v1/equipment-management/equipment-processes/{equipmentProcessId}", equipmentProcess.getEquipmentProcessId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessCode").value(updatedCode));

        EquipmentProcess updatedEquipmentProcess = equipmentProcessRepository.findById(equipmentProcess.getEquipmentProcessId()).orElse(null);
        assertNotNull(updatedEquipmentProcess);
        assertEquals(anotherFactoryLine.getFactoryLineId(), updatedEquipmentProcess.getFactoryLineId());
        assertEquals(updatedCode, updatedEquipmentProcess.getEquipmentProcessCode());
    }

    @Test
    @DisplayName("Delete equipment process API integration success: soft-delete target process")
    void deleteEquipmentProcess_success() throws Exception {
        mockMvc.perform(delete("/api/v1/equipment-management/equipment-processes/{equipmentProcessId}", equipmentProcess.getEquipmentProcessId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        EquipmentProcess deletedEquipmentProcess = equipmentProcessRepository.findById(equipmentProcess.getEquipmentProcessId()).orElse(null);
        assertNotNull(deletedEquipmentProcess);
        assertTrue(deletedEquipmentProcess.getIsDeleted());
    }

    @Test
    @DisplayName("Create equipment API integration success: persist equipment and child tables")
    void createEquipment_success() throws Exception {
        String equipmentCode = "EQ-" + idGenerator.generate();

        EquipmentCreateRequest request = EquipmentCreateRequest.builder()
            .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
            .environmentStandardId(environmentStandard.getEnvironmentStandardId())
            .equipmentCode(equipmentCode)
            .equipmentName("Printing Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.S)
            .equipmentDescription("Integration created equipment")
            .equipmentWarrantyMonth(24)
            .equipmentDesignLifeMonths(120)
            .equipmentWearCoefficient(0.75)
            .equipmentStandardPerformanceRate(98.5)
            .equipmentBaselineErrorRate(1.5)
            .build();

        mockMvc.perform(post("/api/v1/equipment-management/equipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentCode").value(equipmentCode));

        Equipment savedEquipment = equipmentRepository.findByEquipmentCode(equipmentCode).orElse(null);
        assertNotNull(savedEquipment);

        Long agingParamId = equipmentQueryService.getEquipmentAgingParamIdByEquipmentId(savedEquipment.getEquipmentId());
        Long baselineId = equipmentQueryService.getEquipmentBaselineIdByEquipmentId(savedEquipment.getEquipmentId());
        assertNotNull(agingParamId);
        assertNotNull(baselineId);

        EquipmentBaseline savedBaseline = equipmentBaselineRepository.findById(baselineId).orElse(null);
        assertNotNull(savedBaseline);
        assertEquals(new BigDecimal("98.5"), savedBaseline.getEquipmentStandardPerformanceRate());
        assertEquals(new BigDecimal("1.5"), savedBaseline.getEquipmentBaselineErrorRate());
    }

    @Test
    @Commit
    @DisplayName("Create equipment API integration success: commit data to real database for manual verification")
    void createEquipment_success_andCommitToRealDatabase() throws Exception {
        String equipmentCode = "EQ-COMMIT-" + idGenerator.generate();

        EquipmentCreateRequest request = EquipmentCreateRequest.builder()
            .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
            .environmentStandardId(environmentStandard.getEnvironmentStandardId())
            .equipmentCode(equipmentCode)
            .equipmentName("Committed Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.A)
            .equipmentDescription("Committed by integration test")
            .equipmentWarrantyMonth(36)
            .equipmentDesignLifeMonths(180)
            .equipmentWearCoefficient(0.85)
            .equipmentStandardPerformanceRate(99.0)
            .equipmentBaselineErrorRate(1.0)
            .build();

        mockMvc.perform(post("/api/v1/equipment-management/equipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentCode").value(equipmentCode));
    }

    @Test
    @DisplayName("Update equipment API integration success: update equipment and aging parameter")
    void updateEquipment_success() throws Exception {
        Equipment existingEquipment = createEquipmentAggregate("EQ-" + idGenerator.generate(), "Existing Equipment");
        Long agingParamId = equipmentQueryService.getEquipmentAgingParamIdByEquipmentId(existingEquipment.getEquipmentId());

        EquipmentUpdateRequest request = EquipmentUpdateRequest.builder()
            .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
            .environmentStandardId(environmentStandard.getEnvironmentStandardId())
            .equipmentCode("EQ-" + idGenerator.generate())
            .equipmentName("Updated Equipment")
            .equipmentStatus(EquipmentStatus.STOPPED)
            .equipmentGrade(EquipmentGrade.B)
            .equipmentDescription("Updated integration description")
            .equipmentWarrantyMonth(60)
            .equipmentDesignLifeMonths(180)
            .equipmentWearCoefficient(0.9)
            .build();

        mockMvc.perform(put("/api/v1/equipment-management/equipments/{equipmentId}", existingEquipment.getEquipmentId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        Equipment updatedEquipment = equipmentRepository.findById(existingEquipment.getEquipmentId()).orElse(null);
        EquipmentAgingParam updatedAgingParam = equipmentAgingParamRepository.findById(agingParamId).orElse(null);
        assertNotNull(updatedEquipment);
        assertNotNull(updatedAgingParam);
        assertEquals("Updated Equipment", updatedEquipment.getEquipmentName());
        assertEquals(60, updatedAgingParam.getEquipmentWarrantyMonth());
    }

    @Test
    @DisplayName("Delete equipment API integration success: remove equipment and child rows")
    void deleteEquipment_success() throws Exception {
        Equipment existingEquipment = createEquipmentAggregate("EQ-" + idGenerator.generate(), "Delete Target Equipment");
        Long agingParamId = equipmentQueryService.getEquipmentAgingParamIdByEquipmentId(existingEquipment.getEquipmentId());
        Long baselineId = equipmentQueryService.getEquipmentBaselineIdByEquipmentId(existingEquipment.getEquipmentId());

        mockMvc.perform(delete("/api/v1/equipment-management/equipments/{equipmentId}", existingEquipment.getEquipmentId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        assertTrue(equipmentRepository.findById(existingEquipment.getEquipmentId()).isEmpty());
        assertFalse(equipmentAgingParamRepository.findById(agingParamId).isPresent());
        assertFalse(equipmentBaselineRepository.findById(baselineId).isPresent());
    }

    @Test
    @DisplayName("Create environment standard API integration success: persist standard")
    void createEnvironmentStandard_success() throws Exception {
        String environmentCode = "ENV-NEW-" + idGenerator.generate();

        EnvironmentStandardCreateRequest request = EnvironmentStandardCreateRequest.builder()
            .environmentType(EnvironmentType.CLEANROOM)
            .environmentCode(environmentCode)
            .environmentName("Clean Room")
            .envTempMin(BigDecimal.valueOf(18.0))
            .envTempMax(BigDecimal.valueOf(22.0))
            .envHumidityMin(BigDecimal.valueOf(35.0))
            .envHumidityMax(BigDecimal.valueOf(45.0))
            .envParticleLimit(500)
            .build();

        mockMvc.perform(post("/api/v1/equipment-management/environment-standards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.environmentCode").value(environmentCode));

        EnvironmentStandard savedStandard = environmentStandardRepository.findByEnvironmentCode(environmentCode).orElse(null);
        assertNotNull(savedStandard);
        assertFalse(savedStandard.getIsDeleted());
    }

    @Test
    @DisplayName("Update environment standard API integration success: update standard")
    void updateEnvironmentStandard_success() throws Exception {
        EnvironmentStandardUpdateRequest request = EnvironmentStandardUpdateRequest.builder()
            .environmentType(EnvironmentType.CLEANROOM)
            .environmentCode("ENV-UPD-" + idGenerator.generate())
            .environmentName("Updated Clean Room")
            .envTempMin(BigDecimal.valueOf(19.0))
            .envTempMax(BigDecimal.valueOf(23.0))
            .envHumidityMin(BigDecimal.valueOf(33.0))
            .envHumidityMax(BigDecimal.valueOf(43.0))
            .envParticleLimit(600)
            .build();

        mockMvc.perform(put("/api/v1/equipment-management/environment-standards/{environmentStandardId}", environmentStandard.getEnvironmentStandardId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        EnvironmentStandard updatedStandard = environmentStandardRepository.findById(environmentStandard.getEnvironmentStandardId()).orElse(null);
        assertNotNull(updatedStandard);
        assertEquals("Updated Clean Room", updatedStandard.getEnvironmentName());
    }

    @Test
    @DisplayName("Delete environment standard API integration success: soft delete standard")
    void deleteEnvironmentStandard_success() throws Exception {
        mockMvc.perform(delete("/api/v1/equipment-management/environment-standards/{environmentStandardId}", environmentStandard.getEnvironmentStandardId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        EnvironmentStandard deletedStandard = environmentStandardRepository.findById(environmentStandard.getEnvironmentStandardId()).orElse(null);
        assertNotNull(deletedStandard);
        assertTrue(deletedStandard.getIsDeleted());
    }

    @Test
    @DisplayName("Create environment event API integration success: persist event")
    void createEnvironmentEvent_success() throws Exception {
        LocalDateTime detectedAt = LocalDateTime.of(2026, 4, 1, 9, 0);

        EnvironmentEventCreateRequest request = EnvironmentEventCreateRequest.builder()
            .equipmentId(equipment.getEquipmentId())
            .envTemperature(BigDecimal.valueOf(26.5))
            .envHumidity(BigDecimal.valueOf(48.0))
            .envParticleCnt(1500)
            .envDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION)
            .envCorrectionApplied(false)
            .envDetectedAt(detectedAt)
            .build();

        mockMvc.perform(post("/api/v1/equipment-management/environment-events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentId").value(equipment.getEquipmentId()));

        EnvironmentEvent savedEvent = environmentEventRepository.findAll().stream()
            .filter(event -> event.getEquipmentId().equals(equipment.getEquipmentId()))
            .filter(event -> detectedAt.equals(event.getEnvDetectedAt()))
            .findFirst()
            .orElse(null);
        assertNotNull(savedEvent);
    }

    @Test
    @DisplayName("Update environment event API integration success: update event")
    void updateEnvironmentEvent_success() throws Exception {
        EnvironmentEvent environmentEvent = environmentEventRepository.save(
            EnvironmentEvent.builder()
                .environmentEventId(idGenerator.generate())
                .equipmentId(equipment.getEquipmentId())
                .envTemperature(BigDecimal.valueOf(25.1))
                .envHumidity(BigDecimal.valueOf(41.0))
                .envParticleCnt(800)
                .envDeviationType(EnvDeviationType.HUMIDITY_DEVIATION)
                .envCorrectionApplied(false)
                .envDetectedAt(LocalDateTime.of(2026, 4, 1, 8, 30))
                .build()
        );

        EnvironmentEventUpdateRequest request = EnvironmentEventUpdateRequest.builder()
            .equipmentId(equipment.getEquipmentId())
            .envTemperature(BigDecimal.valueOf(27.3))
            .envHumidity(BigDecimal.valueOf(49.0))
            .envParticleCnt(1700)
            .envDeviationType(EnvDeviationType.TEMPERATURE_DEVIATION)
            .envCorrectionApplied(true)
            .envDetectedAt(LocalDateTime.of(2026, 4, 1, 10, 15))
            .build();

        mockMvc.perform(put("/api/v1/equipment-management/environment-events/{environmentEventId}", environmentEvent.getEnvironmentEventId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        EnvironmentEvent updatedEvent = environmentEventRepository.findById(environmentEvent.getEnvironmentEventId()).orElse(null);
        assertNotNull(updatedEvent);
        assertEquals(Integer.valueOf(1700), updatedEvent.getEnvParticleCnt());
        assertTrue(updatedEvent.getEnvCorrectionApplied());
    }

    @Test
    @DisplayName("Delete environment event API integration success: remove event")
    void deleteEnvironmentEvent_success() throws Exception {
        EnvironmentEvent environmentEvent = environmentEventRepository.save(
            EnvironmentEvent.builder()
                .environmentEventId(idGenerator.generate())
                .equipmentId(equipment.getEquipmentId())
                .envTemperature(BigDecimal.valueOf(25.0))
                .envHumidity(BigDecimal.valueOf(42.0))
                .envParticleCnt(900)
                .envDeviationType(EnvDeviationType.HUMIDITY_DEVIATION)
                .envCorrectionApplied(false)
                .envDetectedAt(LocalDateTime.of(2026, 4, 1, 7, 45))
                .build()
        );

        mockMvc.perform(delete("/api/v1/equipment-management/environment-events/{environmentEventId}", environmentEvent.getEnvironmentEventId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        assertTrue(environmentEventRepository.findById(environmentEvent.getEnvironmentEventId()).isEmpty());
    }

    @Test
    @DisplayName("Create maintenance item standard API integration success: persist item standard")
    void createMaintenanceItemStandard_success() throws Exception {
        MaintenanceItemStandardCreateRequest request = MaintenanceItemStandardCreateRequest.builder()
            .maintenanceItem("Sensor Check")
            .maintenanceWeight(new BigDecimal("0.45"))
            .maintenanceScoreMax(new BigDecimal("120.00"))
            .build();

        mockMvc.perform(post("/api/v1/equipment-management/maintenance-item-standards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.maintenanceItem").value("Sensor Check"));

        MaintenanceItemStandard savedItemStandard = maintenanceItemStandardRepository.findAll().stream()
            .filter(item -> "Sensor Check".equals(item.getMaintenanceItem()))
            .findFirst()
            .orElse(null);
        assertNotNull(savedItemStandard);
    }

    @Test
    @DisplayName("Update maintenance item standard API integration success: update item standard")
    void updateMaintenanceItemStandard_success() throws Exception {
        MaintenanceItemStandardUpdateRequest request = MaintenanceItemStandardUpdateRequest.builder()
            .maintenanceItem("Updated Bearing Check")
            .maintenanceWeight(new BigDecimal("0.55"))
            .maintenanceScoreMax(new BigDecimal("130.00"))
            .build();

        mockMvc.perform(put("/api/v1/equipment-management/maintenance-item-standards/{maintenanceItemStandardId}",
                maintenanceItemStandard.getMaintenanceItemStandardId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        MaintenanceItemStandard updatedItemStandard = maintenanceItemStandardRepository
            .findById(maintenanceItemStandard.getMaintenanceItemStandardId()).orElse(null);
        assertNotNull(updatedItemStandard);
        assertEquals("Updated Bearing Check", updatedItemStandard.getMaintenanceItem());
    }

    @Test
    @DisplayName("Delete maintenance item standard API integration success: soft delete item standard")
    void deleteMaintenanceItemStandard_success() throws Exception {
        MaintenanceItemStandard deleteTarget = maintenanceItemStandardRepository.save(
            MaintenanceItemStandard.builder()
                .maintenanceItemStandardId(idGenerator.generate())
                .maintenanceItem("Delete Target Standard")
                .maintenanceWeight(new BigDecimal("0.25"))
                .maintenanceScoreMax(new BigDecimal("90.00"))
                .build()
        );

        mockMvc.perform(delete("/api/v1/equipment-management/maintenance-item-standards/{maintenanceItemStandardId}",
                deleteTarget.getMaintenanceItemStandardId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        MaintenanceItemStandard deletedItemStandard = maintenanceItemStandardRepository.findById(deleteTarget.getMaintenanceItemStandardId())
            .orElse(null);
        assertNotNull(deletedItemStandard);
        assertTrue(deletedItemStandard.getIsDeleted());
    }

    @Test
    @DisplayName("Create maintenance log API integration success: persist maintenance log")
    void createMaintenanceLog_success() throws Exception {
        MaintenanceLogCreateRequest request = MaintenanceLogCreateRequest.builder()
            .equipmentId(equipment.getEquipmentId())
            .maintenanceItemStandardId(maintenanceItemStandard.getMaintenanceItemStandardId())
            .maintenanceType(MaintenanceType.IRREGULAR)
            .maintenanceDate(LocalDate.of(2026, 4, 2))
            .maintenanceScore(new BigDecimal("82.00"))
            .etaMaintDelta(new BigDecimal("5.00"))
            .maintenanceResult(MaintenanceResult.REPAIR_REQUIRED)
            .build();

        mockMvc.perform(post("/api/v1/equipment-management/maintenance-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentId").value(equipment.getEquipmentId()));

        MaintenanceLog savedLog = maintenanceLogRepository.findAll().stream()
            .filter(log -> log.getEquipmentId().equals(equipment.getEquipmentId()))
            .filter(log -> LocalDate.of(2026, 4, 2).equals(log.getMaintenanceDate()))
            .findFirst()
            .orElse(null);
        assertNotNull(savedLog);
    }

    @Test
    @DisplayName("Update maintenance log API integration success: update maintenance log")
    void updateMaintenanceLog_success() throws Exception {
        MaintenanceLogUpdateRequest request = MaintenanceLogUpdateRequest.builder()
            .equipmentId(equipment.getEquipmentId())
            .maintenanceItemStandardId(maintenanceItemStandard.getMaintenanceItemStandardId())
            .maintenanceType(MaintenanceType.IRREGULAR)
            .maintenanceDate(LocalDate.of(2026, 4, 3))
            .maintenanceScore(new BigDecimal("88.00"))
            .etaMaintDelta(new BigDecimal("4.50"))
            .maintenanceResult(MaintenanceResult.REPAIR_COMPLETED)
            .build();

        mockMvc.perform(put("/api/v1/equipment-management/maintenance-logs/{maintenanceLogId}",
                maintenanceLog.getMaintenanceLogId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        MaintenanceLog updatedLog = maintenanceLogRepository.findById(maintenanceLog.getMaintenanceLogId()).orElse(null);
        assertNotNull(updatedLog);
        assertEquals(MaintenanceResult.REPAIR_COMPLETED, updatedLog.getMaintenanceResult());
    }

    @Test
    @DisplayName("Delete maintenance log API integration success: remove maintenance log")
    void deleteMaintenanceLog_success() throws Exception {
        MaintenanceLog deleteTarget = maintenanceLogRepository.save(
            MaintenanceLog.builder()
                .maintenanceLogId(idGenerator.generate())
                .equipmentId(equipment.getEquipmentId())
                .maintenanceItemStandardId(maintenanceItemStandard.getMaintenanceItemStandardId())
                .maintenanceType(MaintenanceType.REGULAR)
                .maintenanceDate(LocalDate.of(2026, 4, 4))
                .maintenanceScore(new BigDecimal("90.00"))
                .etaMaintDelta(new BigDecimal("2.50"))
                .maintenanceResult(MaintenanceResult.NORMAL)
                .build()
        );

        mockMvc.perform(delete("/api/v1/equipment-management/maintenance-logs/{maintenanceLogId}",
                deleteTarget.getMaintenanceLogId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        assertTrue(maintenanceLogRepository.findById(deleteTarget.getMaintenanceLogId()).isEmpty());
    }

    private Equipment createEquipmentAggregate(String equipmentCode, String equipmentName) {
        Long equipmentId = idGenerator.generate();
        Long equipmentAgingParamId = idGenerator.generate();
        Long equipmentBaselineId = idGenerator.generate();

        Equipment createdEquipment = equipmentRepository.save(
            Equipment.builder()
                .equipmentId(equipmentId)
                .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
                .environmentStandardId(environmentStandard.getEnvironmentStandardId())
                .equipmentCode(equipmentCode)
                .equipmentName(equipmentName)
                .equipmentStatus(EquipmentStatus.OPERATING)
                .equipmentGrade(EquipmentGrade.A)
                .equipmentDescription("Seed equipment")
                .build()
        );

        equipmentAgingParamRepository.save(
            EquipmentAgingParam.builder()
                .equipmentAgingParamId(equipmentAgingParamId)
                .equipmentId(equipmentId)
                .equipmentWarrantyMonth(24)
                .equipmentDesignLifeMonths(120)
                .equipmentWearCoefficient(BigDecimal.valueOf(0.75))
                .build()
        );

        equipmentBaselineRepository.save(
            EquipmentBaseline.builder()
                .equipmentBaselineId(equipmentBaselineId)
                .equipmentId(equipmentId)
                .equipmentAgingParamId(equipmentAgingParamId)
                .build()
        );

        entityManager.flush();
        entityManager.clear();
        return createdEquipment;
    }
}
