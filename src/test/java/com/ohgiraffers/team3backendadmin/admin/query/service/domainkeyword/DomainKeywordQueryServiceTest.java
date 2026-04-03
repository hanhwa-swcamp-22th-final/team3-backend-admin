package com.ohgiraffers.team3backendadmin.admin.query.service.domainkeyword;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainCompetencyCategory;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.domainkeyword.DomainKeywordSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.domainkeyword.DomainKeywordDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.domainkeyword.DomainKeywordQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.DomainKeywordQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DomainKeywordQueryServiceTest {

    @Mock
    private DomainKeywordQueryMapper domainKeywordQueryMapper;

    @InjectMocks
    private DomainKeywordQueryService domainKeywordQueryService;

    private DomainKeywordSearchRequest searchRequest;
    private DomainKeywordQueryResponse queryResponse;
    private DomainKeywordDetailResponse detailResponse;

    @BeforeEach
    void setUp() {
        searchRequest = DomainKeywordSearchRequest.builder()
            .keyword("안전")
            .isActive(true)
            .build();

        queryResponse = new DomainKeywordQueryResponse();
        queryResponse.setDomainKeywordId(9001L);
        queryResponse.setDomainKeyword("안전 준수");
        queryResponse.setDomainKeywordDescription("안전 수칙을 지키는 역량");
        queryResponse.setDomainCompetencyCategory(DomainCompetencyCategory.SAFETY);
        queryResponse.setDomainBaseScore(BigDecimal.valueOf(3));
        queryResponse.setDomainWeight(BigDecimal.valueOf(2));
        queryResponse.setDomainIsActive(true);

        detailResponse = new DomainKeywordDetailResponse();
        detailResponse.setDomainKeywordId(9001L);
        detailResponse.setDomainKeyword("안전 준수");
        detailResponse.setDomainKeywordDescription("안전 수칙을 지키는 역량");
        detailResponse.setDomainCompetencyCategory(DomainCompetencyCategory.SAFETY);
        detailResponse.setDomainBaseScore(BigDecimal.valueOf(3));
        detailResponse.setDomainWeight(BigDecimal.valueOf(2));
        detailResponse.setDomainIsActive(true);
    }

    @Test
    @DisplayName("Get domain keyword list success")
    void getDomainKeywordList_success() {
        when(domainKeywordQueryMapper.selectDomainKeywordList(searchRequest)).thenReturn(List.of(queryResponse));

        List<DomainKeywordQueryResponse> result = domainKeywordQueryService.getDomainKeywordList(searchRequest);

        assertEquals(1, result.size());
        assertEquals(9001L, result.get(0).getDomainKeywordId());
        assertEquals("안전 준수", result.get(0).getDomainKeyword());
        verify(domainKeywordQueryMapper).selectDomainKeywordList(searchRequest);
    }

    @Test
    @DisplayName("Get domain keyword list when no data then empty list")
    void getDomainKeywordList_whenNoData_thenReturnEmptyList() {
        DomainKeywordSearchRequest emptyRequest = DomainKeywordSearchRequest.builder().build();
        when(domainKeywordQueryMapper.selectDomainKeywordList(emptyRequest)).thenReturn(List.of());

        List<DomainKeywordQueryResponse> result = domainKeywordQueryService.getDomainKeywordList(emptyRequest);

        assertTrue(result.isEmpty());
        verify(domainKeywordQueryMapper).selectDomainKeywordList(emptyRequest);
    }

    @Test
    @DisplayName("Get domain keyword detail success")
    void getDomainKeywordDetail_success() {
        when(domainKeywordQueryMapper.selectDomainKeywordDetailById(9001L)).thenReturn(detailResponse);

        DomainKeywordDetailResponse result = domainKeywordQueryService.getDomainKeywordDetail(9001L);

        assertEquals(9001L, result.getDomainKeywordId());
        assertEquals("안전 준수", result.getDomainKeyword());
        verify(domainKeywordQueryMapper).selectDomainKeywordDetailById(9001L);
    }

    @Test
    @DisplayName("Get domain keyword detail failure when not found")
    void getDomainKeywordDetail_whenNotFound_thenThrow() {
        when(domainKeywordQueryMapper.selectDomainKeywordDetailById(9999L)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class,
            () -> domainKeywordQueryService.getDomainKeywordDetail(9999L));

        assertEquals(ErrorCode.DOMAIN_KEYWORD_NOT_FOUND, exception.getErrorCode());
        verify(domainKeywordQueryMapper).selectDomainKeywordDetailById(9999L);
    }
}
