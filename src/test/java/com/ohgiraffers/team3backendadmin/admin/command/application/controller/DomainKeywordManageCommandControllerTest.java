package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.domainkeyword.DomainKeywordCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.domainkeyword.DomainKeywordUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.domainkeyword.DomainKeywordCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.domainkeyword.DomainKeywordDeleteResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.domainkeyword.DomainKeywordUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.domainkeyword.DomainKeywordManageCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainCompetencyCategory;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DomainKeywordManageCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
class DomainKeywordManageCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DomainKeywordManageCommandService domainKeywordManageCommandService;

    @Test
    @DisplayName("Create domain keyword API success")
    void createDomainKeyword_success() throws Exception {
        DomainKeywordCreateRequest request = DomainKeywordCreateRequest.builder()
            .domainKeyword("안전 준수")
            .domainKeywordDescription("작업 중 안전 기준 준수")
            .domainCompetencyCategory(DomainCompetencyCategory.SAFETY)
            .domainBaseScore(new BigDecimal("3.00"))
            .domainWeight(new BigDecimal("2.00"))
            .domainIsActive(true)
            .build();

        DomainKeywordCreateResponse response = DomainKeywordCreateResponse.builder()
            .domainKeywordId(11L)
            .domainKeyword("안전 준수")
            .domainKeywordDescription("작업 중 안전 기준 준수")
            .domainCompetencyCategory(DomainCompetencyCategory.SAFETY)
            .domainBaseScore(new BigDecimal("3.00"))
            .domainWeight(new BigDecimal("2.00"))
            .domainIsActive(true)
            .build();

        when(domainKeywordManageCommandService.createDomainKeyword(any(DomainKeywordCreateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/v1/domain-keyword")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.domainKeywordId").value(11L))
            .andExpect(jsonPath("$.data.domainKeyword").value("안전 준수"));
    }

    @Test
    @DisplayName("Create domain keyword API failure when request is invalid")
    void createDomainKeyword_whenInvalidRequest_thenReturn400() throws Exception {
        DomainKeywordCreateRequest request = DomainKeywordCreateRequest.builder()
            .domainKeyword(" ")
            .domainKeywordDescription("작업 중 안전 기준 준수")
            .domainCompetencyCategory(DomainCompetencyCategory.SAFETY)
            .domainBaseScore(new BigDecimal("0.0"))
            .domainWeight(new BigDecimal("0.5"))
            .domainIsActive(true)
            .build();

        mockMvc.perform(post("/api/v1/domain-keyword")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST_001"));
    }

    @Test
    @DisplayName("Update domain keyword API success")
    void updateDomainKeyword_success() throws Exception {
        DomainKeywordUpdateRequest request = DomainKeywordUpdateRequest.builder()
            .domainKeyword("기술 혁신")
            .domainKeywordDescription("기술 혁신을 주도하는 역량")
            .domainCompetencyCategory(DomainCompetencyCategory.INNOVATION)
            .domainBaseScore(new BigDecimal("5.00"))
            .domainWeight(new BigDecimal("3.00"))
            .domainIsActive(false)
            .build();

        DomainKeywordUpdateResponse response = DomainKeywordUpdateResponse.builder()
            .domainKeywordId(11L)
            .domainKeyword("기술 혁신")
            .domainKeywordDescription("기술 혁신을 주도하는 역량")
            .domainCompetencyCategory(DomainCompetencyCategory.INNOVATION)
            .domainBaseScore(new BigDecimal("5.00"))
            .domainWeight(new BigDecimal("3.00"))
            .domainIsActive(false)
            .build();

        when(domainKeywordManageCommandService.updateDomainKeyword(eq(11L), any(DomainKeywordUpdateRequest.class)))
            .thenReturn(response);

        mockMvc.perform(put("/api/v1/domain-keyword/11")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.domainKeywordId").value(11L))
            .andExpect(jsonPath("$.data.domainKeyword").value("기술 혁신"));
    }

    @Test
    @DisplayName("Delete domain keyword API success")
    void deleteDomainKeyword_success() throws Exception {
        DomainKeywordDeleteResponse response = DomainKeywordDeleteResponse.builder()
            .domainKeywordId(11L)
            .deleted(true)
            .build();

        when(domainKeywordManageCommandService.deleteDomainKeyword(11L)).thenReturn(response);

        mockMvc.perform(delete("/api/v1/domain-keyword/11"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.domainKeywordId").value(11L))
            .andExpect(jsonPath("$.data.deleted").value(true));
    }

    @Test
    @DisplayName("Update domain keyword API failure when not found")
    void updateDomainKeyword_whenNotFound_thenReturn404() throws Exception {
        DomainKeywordUpdateRequest request = DomainKeywordUpdateRequest.builder()
            .domainKeyword("기술 혁신")
            .domainKeywordDescription("기술 혁신을 주도하는 역량")
            .domainCompetencyCategory(DomainCompetencyCategory.INNOVATION)
            .domainBaseScore(new BigDecimal("5.00"))
            .domainWeight(new BigDecimal("3.00"))
            .domainIsActive(false)
            .build();

        when(domainKeywordManageCommandService.updateDomainKeyword(eq(999L), any(DomainKeywordUpdateRequest.class)))
            .thenThrow(new BusinessException(ErrorCode.DOMAIN_KEYWORD_NOT_FOUND));

        mockMvc.perform(put("/api/v1/domain-keyword/999")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_013"));
    }
}
