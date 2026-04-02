package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.FactoryLineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineEquipmentStatsResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.FactoryLineQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FactoryLineQueryServiceTest {

    @Mock
    private FactoryLineQueryMapper factoryLineQueryMapper;

    @InjectMocks
    private FactoryLineQueryService factoryLineQueryService;

    private FactoryLineSearchRequest searchRequest;
    private FactoryLineQueryResponse factoryLineQueryResponse;
    private FactoryLineDetailResponse factoryLineDetailResponse;
    private FactoryLineEquipmentStatsResponse factoryLineEquipmentStatsResponse;

    @BeforeEach
    void setUp() {
        searchRequest = FactoryLineSearchRequest.builder()
            .keyword("line")
            .build();

        factoryLineQueryResponse = new FactoryLineQueryResponse();
        factoryLineQueryResponse.setFactoryLineId(1L);
        factoryLineQueryResponse.setFactoryLineCode("LINE-001");
        factoryLineQueryResponse.setFactoryLineName("Main Line");

        factoryLineDetailResponse = new FactoryLineDetailResponse();
        factoryLineDetailResponse.setFactoryLineId(1L);
        factoryLineDetailResponse.setFactoryLineCode("LINE-001");
        factoryLineDetailResponse.setFactoryLineName("Main Line");

        factoryLineEquipmentStatsResponse = new FactoryLineEquipmentStatsResponse();
        factoryLineEquipmentStatsResponse.setTotalEquipmentCount(10L);
        factoryLineEquipmentStatsResponse.setOperatingEquipmentCount(7L);
        factoryLineEquipmentStatsResponse.setOperationRate(new BigDecimal("70.00"));
    }

    @Test
    @DisplayName("Get factory line list success: return a list response DTO")
    void getFactoryLineList_success() {
        when(factoryLineQueryMapper.selectFactoryLineList(searchRequest)).thenReturn(List.of(factoryLineQueryResponse));

        List<FactoryLineQueryResponse> result = factoryLineQueryService.getFactoryLineList(searchRequest);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getFactoryLineId());
        assertEquals("LINE-001", result.get(0).getFactoryLineCode());
        assertEquals("Main Line", result.get(0).getFactoryLineName());
        verify(factoryLineQueryMapper).selectFactoryLineList(searchRequest);
    }

    @Test
    @DisplayName("Get factory line list failure: return an empty list when there is no data")
    void getFactoryLineList_whenNoData_thenReturnEmptyList() {
        FactoryLineSearchRequest emptyRequest = FactoryLineSearchRequest.builder().build();
        when(factoryLineQueryMapper.selectFactoryLineList(emptyRequest)).thenReturn(List.of());

        List<FactoryLineQueryResponse> result = factoryLineQueryService.getFactoryLineList(emptyRequest);

        assertTrue(result.isEmpty());
        verify(factoryLineQueryMapper).selectFactoryLineList(emptyRequest);
    }

    @Test
    @DisplayName("Get factory line detail success: return a detail response DTO")
    void getFactoryLineDetail_success() {
        when(factoryLineQueryMapper.selectFactoryLineDetailById(1L)).thenReturn(factoryLineDetailResponse);

        FactoryLineDetailResponse result = factoryLineQueryService.getFactoryLineDetail(1L);

        assertEquals(1L, result.getFactoryLineId());
        assertEquals("LINE-001", result.getFactoryLineCode());
        assertEquals("Main Line", result.getFactoryLineName());
        verify(factoryLineQueryMapper).selectFactoryLineDetailById(1L);
    }

    @Test
    @DisplayName("Get factory line detail failure: throw business exception when factory line is not found")
    void getFactoryLineDetail_whenNotFound_thenThrow() {
        when(factoryLineQueryMapper.selectFactoryLineDetailById(999L)).thenReturn(null);

        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> factoryLineQueryService.getFactoryLineDetail(999L)
        );

        assertEquals(ErrorCode.FACTORY_LINE_NOT_FOUND, exception.getErrorCode());
        assertEquals("해당 생산 라인을 찾을 수 없습니다.", exception.getMessage());
        verify(factoryLineQueryMapper).selectFactoryLineDetailById(999L);
    }

    @Test
    @DisplayName("Get factory line equipment stats success")
    void getFactoryLineEquipmentStats_success() {
        when(factoryLineQueryMapper.selectFactoryLineEquipmentStats(1L)).thenReturn(factoryLineEquipmentStatsResponse);

        FactoryLineEquipmentStatsResponse result = factoryLineQueryService.getFactoryLineEquipmentStats(1L);

        assertEquals(10L, result.getTotalEquipmentCount());
        assertEquals(7L, result.getOperatingEquipmentCount());
        assertEquals(new BigDecimal("70.00"), result.getOperationRate());
        verify(factoryLineQueryMapper).selectFactoryLineEquipmentStats(1L);
    }

    @Test
    @DisplayName("Check factory line code existence: return true when the code exists")
    void existsByFactoryLineCode_whenExists_thenTrue() {
        when(factoryLineQueryMapper.selectFactoryLineIdByCode("LINE-001")).thenReturn(1L);

        boolean result = factoryLineQueryService.existsByFactoryLineCode("LINE-001");

        assertTrue(result);
        verify(factoryLineQueryMapper).selectFactoryLineIdByCode("LINE-001");
    }

    @Test
    @DisplayName("Check factory line code existence: return false when the code does not exist")
    void existsByFactoryLineCode_whenNotExists_thenFalse() {
        when(factoryLineQueryMapper.selectFactoryLineIdByCode("LINE-404")).thenReturn(null);

        boolean result = factoryLineQueryService.existsByFactoryLineCode("LINE-404");

        org.junit.jupiter.api.Assertions.assertFalse(result);
        verify(factoryLineQueryMapper).selectFactoryLineIdByCode("LINE-404");
    }
}
