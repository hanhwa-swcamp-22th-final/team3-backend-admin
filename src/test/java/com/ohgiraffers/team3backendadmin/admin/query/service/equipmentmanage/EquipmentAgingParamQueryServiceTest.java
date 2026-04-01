package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentAgingParamDetailResponse;
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
class EquipmentAgingParamQueryServiceTest {

    @Mock
    private EquipmentQueryMapper equipmentQueryMapper;

    @InjectMocks
    private EquipmentAgingParamQueryService equipmentAgingParamQueryService;

    @Test
    @DisplayName("Get equipment aging param detail success")
    void getEquipmentAgingParamDetail_success() {
        EquipmentAgingParamDetailResponse response = new EquipmentAgingParamDetailResponse();
        response.setEquipmentAgingParamId(5001L);
        response.setEquipmentId(4001L);
        response.setEquipmentEtaAge(BigDecimal.valueOf(16.5));

        when(equipmentQueryMapper.selectEquipmentAgingParamDetailById(5001L)).thenReturn(response);

        EquipmentAgingParamDetailResponse result = equipmentAgingParamQueryService.getEquipmentAgingParamDetail(5001L);

        assertEquals(5001L, result.getEquipmentAgingParamId());
        assertEquals(4001L, result.getEquipmentId());
        verify(equipmentQueryMapper).selectEquipmentAgingParamDetailById(5001L);
    }

    @Test
    @DisplayName("Get equipment aging param detail failure when not found")
    void getEquipmentAgingParamDetail_whenNotFound_thenThrow() {
        when(equipmentQueryMapper.selectEquipmentAgingParamDetailById(9999L)).thenReturn(null);

        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> equipmentAgingParamQueryService.getEquipmentAgingParamDetail(9999L)
        );

        assertEquals(ErrorCode.EQUIPMENT_AGING_PARAM_NOT_FOUND, exception.getErrorCode());
        verify(equipmentQueryMapper).selectEquipmentAgingParamDetailById(9999L);
    }
}
