package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.FactoryLine;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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
class EquipmentProcessManageCommandControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FactoryLineRepository factoryLineRepository;

    @Autowired
    private EquipmentProcessRepository equipmentProcessRepository;

    @Autowired
    private EntityManager entityManager;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private FactoryLine factoryLine;
    private FactoryLine anotherFactoryLine;
    private EquipmentProcess equipmentProcess;

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
                "Mixing Process"
            )
        );

        entityManager.flush();
        entityManager.clear();
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

        mockMvc.perform(post("/api/v1/admin/equipment-processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineId").value(factoryLine.getFactoryLineId()))
            .andExpect(jsonPath("$.data.equipmentProcessCode").value(equipmentProcessCode))
            .andExpect(jsonPath("$.data.equipmentProcessName").value("Integration Process"));

        EquipmentProcess savedEquipmentProcess =
            equipmentProcessRepository.findByEquipmentProcessCode(equipmentProcessCode).orElse(null);

        assertNotNull(savedEquipmentProcess);
        assertEquals(factoryLine.getFactoryLineId(), savedEquipmentProcess.getFactoryLineId());
        assertEquals("Integration Process", savedEquipmentProcess.getEquipmentProcessName());
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

        mockMvc.perform(put("/api/v1/admin/equipment-processes/{equipmentProcessId}", equipmentProcess.getEquipmentProcessId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessId").value(equipmentProcess.getEquipmentProcessId()))
            .andExpect(jsonPath("$.data.factoryLineId").value(anotherFactoryLine.getFactoryLineId()))
            .andExpect(jsonPath("$.data.equipmentProcessCode").value(updatedCode))
            .andExpect(jsonPath("$.data.equipmentProcessName").value("Updated Process"));

        entityManager.flush();
        entityManager.clear();

        EquipmentProcess updatedEquipmentProcess =
            equipmentProcessRepository.findById(equipmentProcess.getEquipmentProcessId()).orElse(null);

        assertNotNull(updatedEquipmentProcess);
        assertEquals(anotherFactoryLine.getFactoryLineId(), updatedEquipmentProcess.getFactoryLineId());
        assertEquals(updatedCode, updatedEquipmentProcess.getEquipmentProcessCode());
        assertEquals("Updated Process", updatedEquipmentProcess.getEquipmentProcessName());
        assertFalse(updatedEquipmentProcess.getIsDeleted());
    }

    @Test
    @DisplayName("Delete equipment process API integration success: soft-delete target process")
    void deleteEquipmentProcess_success() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/equipment-processes/{equipmentProcessId}", equipmentProcess.getEquipmentProcessId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.equipmentProcessId").value(equipmentProcess.getEquipmentProcessId()))
            .andExpect(jsonPath("$.data.factoryLineId").value(factoryLine.getFactoryLineId()))
            .andExpect(jsonPath("$.data.equipmentProcessCode").value(equipmentProcess.getEquipmentProcessCode()))
            .andExpect(jsonPath("$.data.equipmentProcessName").value(equipmentProcess.getEquipmentProcessName()));

        entityManager.flush();
        entityManager.clear();

        EquipmentProcess deletedEquipmentProcess =
            equipmentProcessRepository.findById(equipmentProcess.getEquipmentProcessId()).orElse(null);

        assertNotNull(deletedEquipmentProcess);
        assertTrue(deletedEquipmentProcess.getIsDeleted());
    }
}
