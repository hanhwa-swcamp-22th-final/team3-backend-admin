package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class EquipmentQueryControllerIntegrationTest {

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
    private EntityManager entityManager;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private Equipment equipment;

    @BeforeEach
    void setUp() {
        FactoryLine factoryLine = factoryLineRepository.save(
            new FactoryLine(
                idGenerator.generate(),
                "LINE-" + idGenerator.generate(),
                "Main Line"
            )
        );

        EquipmentProcess equipmentProcess = equipmentProcessRepository.save(
            new EquipmentProcess(
                idGenerator.generate(),
                factoryLine.getFactoryLineId(),
                "PROC-" + idGenerator.generate(),
                "Drying Process"
            )
        );

        EnvironmentStandard environmentStandard = environmentStandardRepository.save(
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

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Get equipment list API integration success: return persisted equipment")
    void getEquipmentList_success() throws Exception {
        mockMvc.perform(get("/api/v1/admin/equipments")
                .param("keyword", "Query Equipment"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].equipmentId").value(equipment.getEquipmentId()))
            .andExpect(jsonPath("$.data[0].equipmentCode").value(equipment.getEquipmentCode()))
            .andExpect(jsonPath("$.data[0].equipmentName").value("Query Equipment"))
            .andExpect(jsonPath("$.data[0].equipmentStatus").value("OPERATING"))
            .andExpect(jsonPath("$.data[0].equipmentGrade").value("S"));
    }

    @Test
    @DisplayName("Get equipment detail API integration success: return persisted equipment detail")
    void getEquipmentDetail_success() throws Exception {
        mockMvc.perform(get("/api/v1/admin/equipments/{equipmentId}", equipment.getEquipmentId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentId").value(equipment.getEquipmentId()))
            .andExpect(jsonPath("$.data.equipmentCode").value(equipment.getEquipmentCode()))
            .andExpect(jsonPath("$.data.equipmentName").value("Query Equipment"))
            .andExpect(jsonPath("$.data.equipmentDescription").value("Query integration equipment"));
    }
}
