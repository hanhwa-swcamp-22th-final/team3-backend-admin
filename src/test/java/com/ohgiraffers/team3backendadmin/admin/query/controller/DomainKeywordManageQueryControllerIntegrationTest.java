package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainCompetencyCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class DomainKeywordManageQueryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long domainKeywordId;
    private String uniqueSuffix;

    @BeforeEach
    void setUp() {
        domainKeywordId = System.nanoTime();
        uniqueSuffix = String.valueOf(domainKeywordId);

        jdbcTemplate.update(
            "INSERT INTO domain_keyword (domain_keyword_id, domain_keyword, domain_keyword_description, domain_competency_category, domain_base_score, domain_weight, domain_is_active) VALUES (?, ?, ?, ?, ?, ?, ?)",
            domainKeywordId,
            "안전 준수 " + uniqueSuffix,
            "작업 중 안전 기준 준수",
            DomainCompetencyCategory.SAFETY.name(),
            new BigDecimal("3.00"),
            new BigDecimal("2.00"),
            true
        );
    }

    @Test
    @DisplayName("Get domain keyword list API integration success")
    void getDomainKeywordList_success() throws Exception {
        mockMvc.perform(get("/api/v1/domain-keyword")
                .param("keyword", uniqueSuffix)
                .param("category", "SAFETY")
                .param("isActive", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].domainKeywordId").value(domainKeywordId))
            .andExpect(jsonPath("$.data[0].domainKeyword").value("안전 준수 " + uniqueSuffix));
    }

    @Test
    @DisplayName("Get domain keyword detail API integration success")
    void getDomainKeywordDetail_success() throws Exception {
        mockMvc.perform(get("/api/v1/domain-keyword/{domainKeywordId}", domainKeywordId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.domainKeywordId").value(domainKeywordId))
            .andExpect(jsonPath("$.data.domainKeyword").value("안전 준수 " + uniqueSuffix));
    }

    @Test
    @DisplayName("Get domain keyword detail API integration failure when not found")
    void getDomainKeywordDetail_whenNotFound_thenReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/domain-keyword/{domainKeywordId}", -1L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_013"));
    }
}
