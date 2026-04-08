package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.MaintenanceLogSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.MaintenanceLogDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.MaintenanceLogQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.MaintenanceLogQueryMapper;
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
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MaintenanceLogQueryServiceTest {

    @Mock
    private MaintenanceLogQueryMapper maintenanceLogQueryMapper;

    @InjectMocks
    private MaintenanceLogQueryService maintenanceLogQueryService;

    private MaintenanceLogSearchRequest searchRequest;
    private MaintenanceLogQueryResponse queryResponse;
    private MaintenanceLogDetailResponse detailResponse;

    @BeforeEach
    void setUp() {
        searchRequest = MaintenanceLogSearchRequest.builder().equipmentId(4001L).maintenanceType(MaintenanceType.REGULAR).maintenanceResult(MaintenanceResult.NORMAL).build();

        queryResponse = new MaintenanceLogQueryResponse();
        queryResponse.setMaintenanceLogId(8001L);
        queryResponse.setEquipmentId(4001L);
        queryResponse.setEquipmentCode("EQ-001");
        queryResponse.setEquipmentName("Drying Equipment");
        queryResponse.setMaintenanceItemStandardId(7001L);
        queryResponse.setMaintenanceItem("Bearing Inspection");
        queryResponse.setMaintenanceType(MaintenanceType.REGULAR);
        queryResponse.setMaintenanceDate(LocalDate.of(2026, 4, 1));
        queryResponse.setMaintenanceScore(BigDecimal.valueOf(9));
        queryResponse.setEtaMaintDelta(BigDecimal.valueOf(1));
        queryResponse.setMaintenanceResult(MaintenanceResult.NORMAL);

        detailResponse = new MaintenanceLogDetailResponse();
        detailResponse.setMaintenanceLogId(8001L);
        detailResponse.setEquipmentId(4001L);
        detailResponse.setEquipmentCode("EQ-001");
        detailResponse.setEquipmentName("Drying Equipment");
        detailResponse.setMaintenanceItemStandardId(7001L);
        detailResponse.setMaintenanceItem("Bearing Inspection");
        detailResponse.setMaintenanceType(MaintenanceType.REGULAR);
        detailResponse.setMaintenanceDate(LocalDate.of(2026, 4, 1));
        detailResponse.setMaintenanceScore(BigDecimal.valueOf(9));
        detailResponse.setEtaMaintDelta(BigDecimal.valueOf(1));
        detailResponse.setMaintenanceResult(MaintenanceResult.NORMAL);
    }

    @Test
    @DisplayName("Get maintenance log list success")
    void getMaintenanceLogList_success() {
        when(maintenanceLogQueryMapper.selectMaintenanceLogList(searchRequest)).thenReturn(List.of(queryResponse));

        List<MaintenanceLogQueryResponse> result = maintenanceLogQueryService.getMaintenanceLogList(searchRequest);

        assertEquals(1, result.size());
        assertEquals(8001L, result.get(0).getMaintenanceLogId());
        assertEquals(4001L, result.get(0).getEquipmentId());
        verify(maintenanceLogQueryMapper).selectMaintenanceLogList(searchRequest);
    }

    @Test
    @DisplayName("Get maintenance log list when no data then empty list")
    void getMaintenanceLogList_whenNoData_thenReturnEmptyList() {
        MaintenanceLogSearchRequest emptyRequest = MaintenanceLogSearchRequest.builder().build();
        when(maintenanceLogQueryMapper.selectMaintenanceLogList(emptyRequest)).thenReturn(List.of());

        List<MaintenanceLogQueryResponse> result = maintenanceLogQueryService.getMaintenanceLogList(emptyRequest);

        assertTrue(result.isEmpty());
        verify(maintenanceLogQueryMapper).selectMaintenanceLogList(emptyRequest);
    }

    @Test
    @DisplayName("Get maintenance log detail success")
    void getMaintenanceLogDetail_success() {
        when(maintenanceLogQueryMapper.selectMaintenanceLogDetailById(8001L)).thenReturn(detailResponse);

        MaintenanceLogDetailResponse result = maintenanceLogQueryService.getMaintenanceLogDetail(8001L);

        assertEquals(8001L, result.getMaintenanceLogId());
        assertEquals(4001L, result.getEquipmentId());
        verify(maintenanceLogQueryMapper).selectMaintenanceLogDetailById(8001L);
    }

    @Test
    @DisplayName("Get maintenance log detail failure when not found")
    void getMaintenanceLogDetail_whenNotFound_thenThrow() {
        when(maintenanceLogQueryMapper.selectMaintenanceLogDetailById(9999L)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> maintenanceLogQueryService.getMaintenanceLogDetail(9999L));

        assertEquals(ErrorCode.MAINTENANCE_LOG_NOT_FOUND, exception.getErrorCode());
        verify(maintenanceLogQueryMapper).selectMaintenanceLogDetailById(9999L);
    }

    @Test
    @DisplayName("Get latest maintenance log success")
    void getLatestMaintenanceLog_success() {
        when(maintenanceLogQueryMapper.selectLatestMaintenanceLogByEquipmentId(4001L)).thenReturn(detailResponse);

        MaintenanceLogDetailResponse result = maintenanceLogQueryService.getLatestMaintenanceLog(4001L);

        assertEquals(8001L, result.getMaintenanceLogId());
        verify(maintenanceLogQueryMapper).selectLatestMaintenanceLogByEquipmentId(4001L);
    }

    @Test
    @DisplayName("Get abnormal or incomplete maintenance logs success")
    void getAbnormalOrIncompleteMaintenanceLogList_success() {
        when(maintenanceLogQueryMapper.selectAbnormalOrIncompleteMaintenanceLogList()).thenReturn(List.of(queryResponse));

        List<MaintenanceLogQueryResponse> result = maintenanceLogQueryService.getAbnormalOrIncompleteMaintenanceLogList();

        assertEquals(1, result.size());
        verify(maintenanceLogQueryMapper).selectAbnormalOrIncompleteMaintenanceLogList();
    }
}
