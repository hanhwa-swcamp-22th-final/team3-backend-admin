package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EquipmentBaselineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentBaselineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EquipmentQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipmentBaselineQueryServiceTest {

    @Mock
    private EquipmentQueryMapper equipmentQueryMapper;

    @InjectMocks
    private EquipmentBaselineQueryService equipmentBaselineQueryService;

    @Test
    @DisplayName("Get equipment baseline detail success")
    void getEquipmentBaselineDetail_success() {
        EquipmentBaselineDetailResponse response = new EquipmentBaselineDetailResponse();
        response.setEquipmentBaselineId(6001L);
        response.setEquipmentId(4001L);
        response.setEquipmentIdx(BigDecimal.valueOf(90.0));

        when(equipmentQueryMapper.selectEquipmentBaselineDetailById(6001L)).thenReturn(response);

        EquipmentBaselineDetailResponse result = equipmentBaselineQueryService.getEquipmentBaselineDetail(6001L);

        assertEquals(6001L, result.getEquipmentBaselineId());
        assertEquals(4001L, result.getEquipmentId());
        verify(equipmentQueryMapper).selectEquipmentBaselineDetailById(6001L);
    }

    @Test
    @DisplayName("Get equipment baseline detail failure when not found")
    void getEquipmentBaselineDetail_whenNotFound_thenThrow() {
        when(equipmentQueryMapper.selectEquipmentBaselineDetailById(9999L)).thenReturn(null);

        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> equipmentBaselineQueryService.getEquipmentBaselineDetail(9999L)
        );

        assertEquals(ErrorCode.EQUIPMENT_BASELINE_NOT_FOUND, exception.getErrorCode());
        verify(equipmentQueryMapper).selectEquipmentBaselineDetailById(9999L);
    }

    @Test
    @DisplayName("Get latest equipment baseline success")
    void getLatestEquipmentBaseline_success() {
        EquipmentBaselineDetailResponse response = new EquipmentBaselineDetailResponse();
        response.setEquipmentBaselineId(6002L);
        response.setEquipmentId(4001L);

        when(equipmentQueryMapper.selectLatestEquipmentBaselineByEquipmentId(4001L)).thenReturn(response);

        EquipmentBaselineDetailResponse result = equipmentBaselineQueryService.getLatestEquipmentBaseline(4001L);

        assertEquals(6002L, result.getEquipmentBaselineId());
        verify(equipmentQueryMapper).selectLatestEquipmentBaselineByEquipmentId(4001L);
    }

    @Test
    @DisplayName("Get equipment baseline history success")
    void getEquipmentBaselineHistory_success() {
        EquipmentBaselineSearchRequest request = EquipmentBaselineSearchRequest.builder()
            .equipmentId(4001L)
            .calculatedFrom(LocalDateTime.of(2026, 4, 1, 0, 0))
            .build();
        EquipmentBaselineDetailResponse response = new EquipmentBaselineDetailResponse();
        response.setEquipmentBaselineId(6003L);

        when(equipmentQueryMapper.selectEquipmentBaselineHistory(request)).thenReturn(List.of(response));

        List<EquipmentBaselineDetailResponse> result = equipmentBaselineQueryService.getEquipmentBaselineHistory(request);

        assertFalse(result.isEmpty());
        assertEquals(6003L, result.get(0).getEquipmentBaselineId());
        verify(equipmentQueryMapper).selectEquipmentBaselineHistory(request);
    }
}
