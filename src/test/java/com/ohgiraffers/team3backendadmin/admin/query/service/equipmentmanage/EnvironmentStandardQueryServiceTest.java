package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentType;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EnvironmentStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EnvironmentStandardQueryMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnvironmentStandardQueryServiceTest {

    @Mock
    private EnvironmentStandardQueryMapper environmentStandardQueryMapper;

    @InjectMocks
    private EnvironmentStandardQueryService environmentStandardQueryService;

    private EnvironmentStandardSearchRequest searchRequest;
    private EnvironmentStandardQueryResponse environmentStandardQueryResponse;
    private EnvironmentStandardDetailResponse environmentStandardDetailResponse;

    @BeforeEach
    void setUp() {
        searchRequest = EnvironmentStandardSearchRequest.builder()
            .keyword("dry")
            .environmentType(EnvironmentType.DRYROOM)
            .build();

        environmentStandardQueryResponse = new EnvironmentStandardQueryResponse();
        environmentStandardQueryResponse.setEnvironmentStandardId(1L);
        environmentStandardQueryResponse.setEnvironmentType(EnvironmentType.DRYROOM);
        environmentStandardQueryResponse.setEnvironmentCode("ENV-001");
        environmentStandardQueryResponse.setEnvironmentName("Dry Room");

        environmentStandardDetailResponse = new EnvironmentStandardDetailResponse();
        environmentStandardDetailResponse.setEnvironmentStandardId(1L);
        environmentStandardDetailResponse.setEnvironmentType(EnvironmentType.DRYROOM);
        environmentStandardDetailResponse.setEnvironmentCode("ENV-001");
        environmentStandardDetailResponse.setEnvironmentName("Dry Room");
    }

    @Test
    @DisplayName("Get environment standard list success: return a list response DTO")
    void getEnvironmentStandardList_success() {
        when(environmentStandardQueryMapper.selectEnvironmentStandardList(searchRequest))
            .thenReturn(List.of(environmentStandardQueryResponse));

        List<EnvironmentStandardQueryResponse> result = environmentStandardQueryService.getEnvironmentStandardList(searchRequest);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getEnvironmentStandardId());
        assertEquals(EnvironmentType.DRYROOM, result.get(0).getEnvironmentType());
        assertEquals("ENV-001", result.get(0).getEnvironmentCode());
        verify(environmentStandardQueryMapper).selectEnvironmentStandardList(searchRequest);
    }

    @Test
    @DisplayName("Get environment standard list failure: return an empty list when there is no data")
    void getEnvironmentStandardList_whenNoData_thenReturnEmptyList() {
        EnvironmentStandardSearchRequest emptyRequest = EnvironmentStandardSearchRequest.builder().build();
        when(environmentStandardQueryMapper.selectEnvironmentStandardList(emptyRequest)).thenReturn(List.of());

        List<EnvironmentStandardQueryResponse> result = environmentStandardQueryService.getEnvironmentStandardList(emptyRequest);

        assertTrue(result.isEmpty());
        verify(environmentStandardQueryMapper).selectEnvironmentStandardList(emptyRequest);
    }

    @Test
    @DisplayName("Get environment standard detail success: return a detail response DTO")
    void getEnvironmentStandardDetail_success() {
        when(environmentStandardQueryMapper.selectEnvironmentStandardDetailById(1L)).thenReturn(environmentStandardDetailResponse);

        EnvironmentStandardDetailResponse result = environmentStandardQueryService.getEnvironmentStandardDetail(1L);

        assertNotNull(result);
        assertEquals(1L, result.getEnvironmentStandardId());
        assertEquals(EnvironmentType.DRYROOM, result.getEnvironmentType());
        assertEquals("ENV-001", result.getEnvironmentCode());
        verify(environmentStandardQueryMapper).selectEnvironmentStandardDetailById(1L);
    }

    @Test
    @DisplayName("Get environment standard detail failure: return null when environment standard is not found")
    void getEnvironmentStandardDetail_whenNotFound_thenNull() {
        when(environmentStandardQueryMapper.selectEnvironmentStandardDetailById(999L)).thenReturn(null);

        EnvironmentStandardDetailResponse result = environmentStandardQueryService.getEnvironmentStandardDetail(999L);

        assertNull(result);
        verify(environmentStandardQueryMapper).selectEnvironmentStandardDetailById(999L);
    }

    @Test
    @DisplayName("Check environment standard code existence: return true when the code exists")
    void existsByEnvironmentCode_whenExists_thenTrue() {
        when(environmentStandardQueryMapper.selectEnvironmentStandardIdByCode("ENV-001")).thenReturn(1L);

        boolean result = environmentStandardQueryService.existsByEnvironmentCode("ENV-001");

        assertTrue(result);
        verify(environmentStandardQueryMapper).selectEnvironmentStandardIdByCode("ENV-001");
    }

    @Test
    @DisplayName("Check environment standard code existence: return false when the code does not exist")
    void existsByEnvironmentCode_whenNotExists_thenFalse() {
        when(environmentStandardQueryMapper.selectEnvironmentStandardIdByCode("ENV-404")).thenReturn(null);

        boolean result = environmentStandardQueryService.existsByEnvironmentCode("ENV-404");

        assertFalse(result);
        verify(environmentStandardQueryMapper).selectEnvironmentStandardIdByCode("ENV-404");
    }
}