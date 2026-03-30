package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentAgingParam;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentBaseline;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.FactoryLine;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentStandardRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentAgingParamRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentBaselineRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentProcessRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.FactoryLineRepository;
import com.ohgiraffers.team3backendadmin.admin.query.service.EquipmentQueryService;
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
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentAgingParamRepository equipmentAgingParamRepository;

    @Autowired
    private EquipmentBaselineRepository equipmentBaselineRepository;

    @Autowired
    private EquipmentQueryService equipmentQueryService;

    @Autowired
    private EntityManager entityManager;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private FactoryLine factoryLine;
    private EquipmentProcess equipmentProcess;
    private EnvironmentStandard environmentStandard;

    @BeforeEach
    void setUp() {
        factoryLine = factoryLineRepository.save(
            new FactoryLine(
                idGenerator.generate(),
                "LINE-" + idGenerator.generate(),
                "Main Line"
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
                .enviromentCode("ENV-" + idGenerator.generate())
                .enviromentName("Dry Room")
                .envTempMin(BigDecimal.valueOf(20.0))
                .envTempMax(BigDecimal.valueOf(25.0))
                .envHumidityMin(BigDecimal.valueOf(30.0))
                .envHumidityMax(BigDecimal.valueOf(40.0))
                .envParticleLimit(1000)
                .build()
        );

        entityManager.flush();
        entityManager.clear();
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
            .build();

        mockMvc.perform(post("/api/v1/equipment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentCode").value(equipmentCode))
            .andExpect(jsonPath("$.data.equipmentName").value("Printing Equipment"))
            .andExpect(jsonPath("$.data.equipmentStatus").value("OPERATING"))
            .andExpect(jsonPath("$.data.equipmentGrade").value("S"));

        Equipment savedEquipment = equipmentRepository.findByEquipmentCode(equipmentCode).orElse(null);
        assertNotNull(savedEquipment);

        Long agingParamId = equipmentQueryService.getEquipmentAgingParamIdByEquipmentId(savedEquipment.getEquipmentId());
        Long baselineId = equipmentQueryService.getEquipmentBaselineIdByEquipmentId(savedEquipment.getEquipmentId());

        assertNotNull(agingParamId);
        assertNotNull(baselineId);

        EquipmentAgingParam equipmentAgingParam = equipmentAgingParamRepository.findById(agingParamId).orElse(null);
        EquipmentBaseline equipmentBaseline = equipmentBaselineRepository.findById(baselineId).orElse(null);

        assertNotNull(equipmentAgingParam);
        assertNotNull(equipmentBaseline);
        assertEquals(24, equipmentAgingParam.getEquipmentWarrantyMonth());
        assertEquals(120, equipmentAgingParam.getEquipmentDesignLifeMonths());
        assertEquals(BigDecimal.valueOf(0.75), equipmentAgingParam.getEquipmentWearCoefficient());
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
            .build();

        mockMvc.perform(post("/api/v1/equipment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentCode").value(equipmentCode))
            .andExpect(jsonPath("$.data.equipmentName").value("Committed Equipment"));

        Equipment savedEquipment = equipmentRepository.findByEquipmentCode(equipmentCode).orElse(null);
        assertNotNull(savedEquipment);

        Long agingParamId = equipmentQueryService.getEquipmentAgingParamIdByEquipmentId(savedEquipment.getEquipmentId());
        Long baselineId = equipmentQueryService.getEquipmentBaselineIdByEquipmentId(savedEquipment.getEquipmentId());

        assertNotNull(agingParamId);
        assertNotNull(baselineId);
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

        mockMvc.perform(put("/api/v1/equipment/{equipmentId}", existingEquipment.getEquipmentId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        Equipment updatedEquipment = equipmentRepository.findById(existingEquipment.getEquipmentId()).orElse(null);
        EquipmentAgingParam updatedAgingParam = equipmentAgingParamRepository.findById(agingParamId).orElse(null);

        assertNotNull(updatedEquipment);
        assertNotNull(updatedAgingParam);
        assertEquals("Updated Equipment", updatedEquipment.getEquipmentName());
        assertEquals(EquipmentStatus.STOPPED, updatedEquipment.getEquipmentStatus());
        assertEquals(EquipmentGrade.B, updatedEquipment.getEquipmentGrade());
        assertEquals("Updated integration description", updatedEquipment.getEquipmentDescription());
        assertEquals(60, updatedAgingParam.getEquipmentWarrantyMonth());
        assertEquals(180, updatedAgingParam.getEquipmentDesignLifeMonths());
        assertEquals(BigDecimal.valueOf(0.9), updatedAgingParam.getEquipmentWearCoefficient());
    }

    @Test
    @DisplayName("Delete equipment API integration success: remove equipment and child rows")
    void deleteEquipment_success() throws Exception {
        Equipment existingEquipment = createEquipmentAggregate("EQ-" + idGenerator.generate(), "Delete Target Equipment");
        Long agingParamId = equipmentQueryService.getEquipmentAgingParamIdByEquipmentId(existingEquipment.getEquipmentId());
        Long baselineId = equipmentQueryService.getEquipmentBaselineIdByEquipmentId(existingEquipment.getEquipmentId());

        mockMvc.perform(delete("/api/v1/equipment/{equipmentId}", existingEquipment.getEquipmentId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        assertTrue(equipmentRepository.findById(existingEquipment.getEquipmentId()).isEmpty());
        assertFalse(equipmentAgingParamRepository.findById(agingParamId).isPresent());
        assertFalse(equipmentBaselineRepository.findById(baselineId).isPresent());
    }

    private Equipment createEquipmentAggregate(String equipmentCode, String equipmentName) {
        Long equipmentId = idGenerator.generate();
        Long equipmentAgingParamId = idGenerator.generate();
        Long equipmentBaselineId = idGenerator.generate();

        Equipment equipment = equipmentRepository.save(
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
        return equipment;
    }
}
