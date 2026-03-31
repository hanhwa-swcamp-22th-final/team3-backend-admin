package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.MaintenanceItemStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceItemStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceItemStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.MaintenanceItemStandardQueryMapper;
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
class MaintenanceItemStandardQueryServiceTest {

    @Mock
    private MaintenanceItemStandardQueryMapper maintenanceItemStandardQueryMapper;

    @InjectMocks
    private MaintenanceItemStandardQueryService maintenanceItemStandardQueryService;

    private MaintenanceItemStandardSearchRequest searchRequest;
    private MaintenanceItemStandardQueryResponse queryResponse;
    private MaintenanceItemStandardDetailResponse detailResponse;

    @BeforeEach
    void setUp() {
        searchRequest = MaintenanceItemStandardSearchRequest.builder().keyword("Bearing").build();

        queryResponse = new MaintenanceItemStandardQueryResponse();
        queryResponse.setMaintenanceItemStandardId(7001L);
        queryResponse.setMaintenanceItem("Bearing Inspection");
        queryResponse.setMaintenanceWeight(BigDecimal.valueOf(2));
        queryResponse.setMaintenanceScoreMax(BigDecimal.valueOf(10));

        detailResponse = new MaintenanceItemStandardDetailResponse();
        detailResponse.setMaintenanceItemStandardId(7001L);
        detailResponse.setMaintenanceItem("Bearing Inspection");
        detailResponse.setMaintenanceWeight(BigDecimal.valueOf(2));
        detailResponse.setMaintenanceScoreMax(BigDecimal.valueOf(10));
    }

    @Test
    @DisplayName("Get maintenance item standard list success")
    void getMaintenanceItemStandardList_success() {
        when(maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardList(searchRequest)).thenReturn(List.of(queryResponse));

        List<MaintenanceItemStandardQueryResponse> result = maintenanceItemStandardQueryService.getMaintenanceItemStandardList(searchRequest);

        assertEquals(1, result.size());
        assertEquals(7001L, result.get(0).getMaintenanceItemStandardId());
        assertEquals("Bearing Inspection", result.get(0).getMaintenanceItem());
        verify(maintenanceItemStandardQueryMapper).selectMaintenanceItemStandardList(searchRequest);
    }

    @Test
    @DisplayName("Get maintenance item standard list when no data then empty list")
    void getMaintenanceItemStandardList_whenNoData_thenReturnEmptyList() {
        MaintenanceItemStandardSearchRequest emptyRequest = MaintenanceItemStandardSearchRequest.builder().build();
        when(maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardList(emptyRequest)).thenReturn(List.of());

        List<MaintenanceItemStandardQueryResponse> result = maintenanceItemStandardQueryService.getMaintenanceItemStandardList(emptyRequest);

        assertTrue(result.isEmpty());
        verify(maintenanceItemStandardQueryMapper).selectMaintenanceItemStandardList(emptyRequest);
    }

    @Test
    @DisplayName("Get maintenance item standard detail success")
    void getMaintenanceItemStandardDetail_success() {
        when(maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardDetailById(7001L)).thenReturn(detailResponse);

        MaintenanceItemStandardDetailResponse result = maintenanceItemStandardQueryService.getMaintenanceItemStandardDetail(7001L);

        assertEquals(7001L, result.getMaintenanceItemStandardId());
        assertEquals("Bearing Inspection", result.getMaintenanceItem());
        verify(maintenanceItemStandardQueryMapper).selectMaintenanceItemStandardDetailById(7001L);
    }

    @Test
    @DisplayName("Get maintenance item standard detail failure when not found")
    void getMaintenanceItemStandardDetail_whenNotFound_thenThrow() {
        when(maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardDetailById(9999L)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> maintenanceItemStandardQueryService.getMaintenanceItemStandardDetail(9999L));

        assertEquals(ErrorCode.MAINTENANCE_ITEM_STANDARD_NOT_FOUND, exception.getErrorCode());
        verify(maintenanceItemStandardQueryMapper).selectMaintenanceItemStandardDetailById(9999L);
    }
}