package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainCompetencyCategory;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.domainkeyword.DomainKeywordDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.domainkeyword.DomainKeywordQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.service.domainkeyword.DomainKeywordQueryService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DomainKeywordManageQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class DomainKeywordManageQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DomainKeywordQueryService domainKeywordQueryService;

    @Test
    @DisplayName("Get domain keyword list API success")
    void getDomainKeywordList_success() throws Exception {
        DomainKeywordQueryResponse response = new DomainKeywordQueryResponse();
        response.setDomainKeywordId(11L);
        response.setDomainKeyword("안전 준수");
        response.setDomainKeywordDescription("작업 중 안전 기준 준수");
        response.setDomainCompetencyCategory(DomainCompetencyCategory.SAFETY);
        response.setDomainBaseScore(new BigDecimal("3.00"));
        response.setDomainWeight(new BigDecimal("2.00"));
        response.setDomainIsActive(true);

        when(domainKeywordQueryService.getDomainKeywordList(argThat(request -> request != null)))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/domain-keyword"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].domainKeywordId").value(11L))
            .andExpect(jsonPath("$.data[0].domainKeyword").value("안전 준수"));
    }

    @Test
    @DisplayName("Get domain keyword list API success with query params")
    void getDomainKeywordList_withQueryParams_success() throws Exception {
        when(domainKeywordQueryService.getDomainKeywordList(argThat(request -> request != null)))
            .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/domain-keyword")
                .param("keyword", "안전")
                .param("category", "SAFETY")
                .param("isActive", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));

        verify(domainKeywordQueryService).getDomainKeywordList(argThat(request ->
            "안전".equals(request.getKeyword())
                && DomainCompetencyCategory.SAFETY == request.getCategory()
                && Boolean.TRUE.equals(request.getIsActive())
        ));
    }

    @Test
    @DisplayName("Get domain keyword detail API success")
    void getDomainKeywordDetail_success() throws Exception {
        DomainKeywordDetailResponse response = new DomainKeywordDetailResponse();
        response.setDomainKeywordId(11L);
        response.setDomainKeyword("안전 준수");
        response.setDomainKeywordDescription("작업 중 안전 기준 준수");
        response.setDomainCompetencyCategory(DomainCompetencyCategory.SAFETY);
        response.setDomainBaseScore(new BigDecimal("3.00"));
        response.setDomainWeight(new BigDecimal("2.00"));
        response.setDomainIsActive(true);

        when(domainKeywordQueryService.getDomainKeywordDetail(11L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/domain-keyword/11"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.domainKeywordId").value(11L))
            .andExpect(jsonPath("$.data.domainKeyword").value("안전 준수"));
    }

    @Test
    @DisplayName("Get domain keyword detail API failure when not found")
    void getDomainKeywordDetail_whenNotFound_thenReturn404() throws Exception {
        when(domainKeywordQueryService.getDomainKeywordDetail(999L))
            .thenThrow(new BusinessException(ErrorCode.DOMAIN_KEYWORD_NOT_FOUND));

        mockMvc.perform(get("/api/v1/domain-keyword/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorCode").value("NOT_FOUND_013"));
    }
}
