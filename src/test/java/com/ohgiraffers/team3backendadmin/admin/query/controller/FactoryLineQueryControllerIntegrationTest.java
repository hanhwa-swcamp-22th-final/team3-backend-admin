package com.ohgiraffers.team3backendadmin.admin.query.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class FactoryLineQueryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
                "Query Main Line"
            )
        );

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Get factory line list API integration success: return persisted factory line")
    void getFactoryLineList_success() throws Exception {
        mockMvc.perform(get("/api/v1/factory-lines")
                .param("keyword", "Query Main"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].factoryLineId").value(factoryLine.getFactoryLineId()))
            .andExpect(jsonPath("$.data[0].factoryLineCode").value(factoryLine.getFactoryLineCode()))
            .andExpect(jsonPath("$.data[0].factoryLineName").value("Query Main Line"));
    }

    @Test
    @DisplayName("Get factory line detail API integration success: return persisted factory line detail")
    void getFactoryLineDetail_success() throws Exception {
        mockMvc.perform(get("/api/v1/factory-lines/{factoryLineId}", factoryLine.getFactoryLineId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.factoryLineId").value(factoryLine.getFactoryLineId()))
            .andExpect(jsonPath("$.data.factoryLineCode").value(factoryLine.getFactoryLineCode()))
            .andExpect(jsonPath("$.data.factoryLineName").value("Query Main Line"));
    }
}
