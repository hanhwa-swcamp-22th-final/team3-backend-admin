package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.equipmentmanage.EquipmentAgingParamUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.equipmentmanage.EquipmentAgingParamUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentAgingParam;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentAgingParamRepository;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipmentAgingParamManageCommandServiceTest {

    @Mock
    private EquipmentAgingParamRepository equipmentAgingParamRepository;

    @Mock
    private EquipmentReferenceSnapshotCommandService equipmentReferenceSnapshotCommandService;

    @InjectMocks
    private EquipmentAgingParamManageCommandService equipmentAgingParamManageCommandService;

    private EquipmentAgingParam equipmentAgingParam;
    private EquipmentAgingParamUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        equipmentAgingParam = EquipmentAgingParam.builder()
            .equipmentAgingParamId(5001L)
            .equipmentId(4001L)
            .equipmentWarrantyMonth(24)
            .equipmentDesignLifeMonths(120)
            .equipmentWearCoefficient(BigDecimal.valueOf(0.75))
            .build();

        updateRequest = EquipmentAgingParamUpdateRequest.builder()
            .equipmentEtaAge(BigDecimal.valueOf(16.5))
            .equipmentWarrantyMonth(36)
            .equipmentDesignLifeMonths(180)
            .equipmentWearCoefficient(BigDecimal.valueOf(0.85))
            .equipmentAgeMonths(42)
            .equipmentAgeCalculatedAt(LocalDateTime.of(2026, 4, 1, 10, 0))
            .build();
    }

    @Test
    @DisplayName("Update equipment aging param success")
    void updateEquipmentAgingParam_success() {
        when(equipmentAgingParamRepository.findById(5001L)).thenReturn(Optional.of(equipmentAgingParam));

        EquipmentAgingParamUpdateResponse response =
            equipmentAgingParamManageCommandService.updateEquipmentAgingParam(5001L, updateRequest);

        assertEquals(5001L, response.getEquipmentAgingParamId());
        assertEquals(4001L, response.getEquipmentId());
        assertEquals(BigDecimal.valueOf(16.5), response.getEquipmentEtaAge());
        assertEquals(36, response.getEquipmentWarrantyMonth());
        assertEquals(180, response.getEquipmentDesignLifeMonths());
        assertEquals(BigDecimal.valueOf(0.85), response.getEquipmentWearCoefficient());
        assertEquals(42, response.getEquipmentAgeMonths());
    }

    @Test
    @DisplayName("Update equipment aging param failure when not found")
    void updateEquipmentAgingParam_whenNotFound_thenThrow() {
        when(equipmentAgingParamRepository.findById(9999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> equipmentAgingParamManageCommandService.updateEquipmentAgingParam(9999L, updateRequest)
        );

        assertEquals(ErrorCode.EQUIPMENT_AGING_PARAM_NOT_FOUND, exception.getErrorCode());
    }
}
