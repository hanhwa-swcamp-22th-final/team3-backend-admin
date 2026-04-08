package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.domainkeyword.DomainKeywordCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.domainkeyword.DomainKeywordUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainCompetencyCategory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainKeyword;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DomainKeywordRepository;
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
class DomainKeywordManageCommandControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DomainKeywordRepository domainKeywordRepository;

    @Autowired
    private EntityManager entityManager;

    private final TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

    private DomainKeyword domainKeyword;

    @BeforeEach
    void setUp() {
        domainKeyword = domainKeywordRepository.save(
            DomainKeyword.builder()
                .domainKeywordId(idGenerator.generate())
                .domainKeyword("안전 준수")
                .domainKeywordDescription("작업 중 안전 기준 준수")
                .domainCompetencyCategory(DomainCompetencyCategory.SAFETY)
                .domainBaseScore(new BigDecimal("3.00"))
                .domainWeight(new BigDecimal("2.00"))
                .domainIsActive(true)
                .build()
        );

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Create domain keyword API integration success")
    void createDomainKeyword_success() throws Exception {
        DomainKeywordCreateRequest request = DomainKeywordCreateRequest.builder()
            .domainKeyword("기술 혁신")
            .domainKeywordDescription("기술 혁신을 주도하는 역량")
            .domainCompetencyCategory(DomainCompetencyCategory.INNOVATION)
            .domainBaseScore(new BigDecimal("5.00"))
            .domainWeight(new BigDecimal("3.00"))
            .domainIsActive(true)
            .build();

        mockMvc.perform(post("/api/v1/domain-keyword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.domainKeyword").value("기술 혁신"));

        DomainKeyword savedKeyword = domainKeywordRepository.findAll().stream()
            .filter(item -> "기술 혁신".equals(item.getDomainKeyword()))
            .findFirst()
            .orElse(null);

        assertNotNull(savedKeyword);
        assertEquals("기술 혁신을 주도하는 역량", savedKeyword.getDomainKeywordDescription());
    }

    @Test
    @DisplayName("Update domain keyword API integration success")
    void updateDomainKeyword_success() throws Exception {
        DomainKeywordUpdateRequest request = DomainKeywordUpdateRequest.builder()
            .domainKeyword("협업 강화")
            .domainKeywordDescription("팀 간 협업을 촉진하는 역량")
            .domainCompetencyCategory(DomainCompetencyCategory.OTHER)
            .domainBaseScore(new BigDecimal("4.00"))
            .domainWeight(new BigDecimal("2.50"))
            .domainIsActive(false)
            .build();

        mockMvc.perform(put("/api/v1/domain-keyword/{domainKeywordId}", domainKeyword.getDomainKeywordId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.domainKeyword").value("협업 강화"));

        DomainKeyword updatedKeyword = domainKeywordRepository.findById(domainKeyword.getDomainKeywordId()).orElse(null);
        assertNotNull(updatedKeyword);
        assertEquals("협업 강화", updatedKeyword.getDomainKeyword());
        assertFalse(updatedKeyword.getDomainIsActive());
    }

    @Test
    @DisplayName("Delete domain keyword API integration success")
    void deleteDomainKeyword_success() throws Exception {
        mockMvc.perform(delete("/api/v1/domain-keyword/{domainKeywordId}", domainKeyword.getDomainKeywordId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.deleted").value(true));

        assertTrue(domainKeywordRepository.findById(domainKeyword.getDomainKeywordId()).isEmpty());
    }
}
