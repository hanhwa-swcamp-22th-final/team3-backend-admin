package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentProcessRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class EquipmentProcessQueryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FactoryLineRepository factoryLineRepository;

    @Autowired
    private EquipmentProcessRepository equipmentProcessRepository;

    @Autowired
    private EntityManager entityManager;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private FactoryLine factoryLine;
    private EquipmentProcess equipmentProcess;

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
                "Query Mixing Process"
            )
        );

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Get equipment process list API integration success: return persisted equipment process")
    void getEquipmentProcessList_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipment-processes")
                .param("factoryLineId", String.valueOf(factoryLine.getFactoryLineId()))
                .param("keyword", "Query Mixing"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].equipmentProcessId").value(equipmentProcess.getEquipmentProcessId()))
            .andExpect(jsonPath("$.data[0].factoryLineId").value(factoryLine.getFactoryLineId()))
            .andExpect(jsonPath("$.data[0].factoryLineCode").value(factoryLine.getFactoryLineCode()))
            .andExpect(jsonPath("$.data[0].factoryLineName").value("Query Line"))
            .andExpect(jsonPath("$.data[0].equipmentProcessCode").value(equipmentProcess.getEquipmentProcessCode()))
            .andExpect(jsonPath("$.data[0].equipmentProcessName").value("Query Mixing Process"));
    }

    @Test
    @DisplayName("Get equipment process detail API integration success: return persisted equipment process detail")
    void getEquipmentProcessDetail_success() throws Exception {
        mockMvc.perform(get("/api/v1/equipment-management/equipment-processes/{equipmentProcessId}", equipmentProcess.getEquipmentProcessId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessId").value(equipmentProcess.getEquipmentProcessId()))
            .andExpect(jsonPath("$.data.factoryLineId").value(factoryLine.getFactoryLineId()))
            .andExpect(jsonPath("$.data.factoryLineCode").value(factoryLine.getFactoryLineCode()))
            .andExpect(jsonPath("$.data.factoryLineName").value("Query Line"))
            .andExpect(jsonPath("$.data.equipmentProcessCode").value(equipmentProcess.getEquipmentProcessCode()))
            .andExpect(jsonPath("$.data.equipmentProcessName").value("Query Mixing Process"));
    }
}
