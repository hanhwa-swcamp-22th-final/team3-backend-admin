package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.FactoryLine;
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
class FactoryLineManageCommandControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FactoryLineRepository factoryLineRepository;

    @Autowired
    private EntityManager entityManager;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private FactoryLine factoryLine;

    @BeforeEach
    void setUp() {
        factoryLine = factoryLineRepository.save(
            new FactoryLine(
                idGenerator.generate(),
                "LINE-" + idGenerator.generate(),
                "Main Line"
            )
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

        mockMvc.perform(post("/api/v1/admin/factory-lines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineCode").value(factoryLineCode))
            .andExpect(jsonPath("$.data.factoryLineName").value("Integration Line"));

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

        mockMvc.perform(put("/api/v1/admin/factory-lines/{factoryLineId}", factoryLine.getFactoryLineId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineId").value(factoryLine.getFactoryLineId()))
            .andExpect(jsonPath("$.data.factoryLineCode").value(updatedCode))
            .andExpect(jsonPath("$.data.factoryLineName").value("Updated Line"));

        entityManager.flush();
        entityManager.clear();

        FactoryLine updatedFactoryLine = factoryLineRepository.findById(factoryLine.getFactoryLineId()).orElse(null);

        assertNotNull(updatedFactoryLine);
        assertEquals(updatedCode, updatedFactoryLine.getFactoryLineCode());
        assertEquals("Updated Line", updatedFactoryLine.getFactoryLineName());
        assertFalse(updatedFactoryLine.getIsDeleted());
    }

    @Test
    @DisplayName("Delete factory line API integration success: soft-delete target factory line")
    void deleteFactoryLine_success() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/factory-lines/{factoryLineId}", factoryLine.getFactoryLineId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineId").value(factoryLine.getFactoryLineId()))
            .andExpect(jsonPath("$.data.factoryLineCode").value(factoryLine.getFactoryLineCode()))
            .andExpect(jsonPath("$.data.factoryLineName").value(factoryLine.getFactoryLineName()));

        entityManager.flush();
        entityManager.clear();

        FactoryLine deletedFactoryLine = factoryLineRepository.findById(factoryLine.getFactoryLineId()).orElse(null);

        assertNotNull(deletedFactoryLine);
        assertTrue(deletedFactoryLine.getIsDeleted());
    }
}
