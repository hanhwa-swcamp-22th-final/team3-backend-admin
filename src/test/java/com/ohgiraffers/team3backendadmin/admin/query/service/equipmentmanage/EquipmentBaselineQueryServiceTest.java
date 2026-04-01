package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentBaselineDetailResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
