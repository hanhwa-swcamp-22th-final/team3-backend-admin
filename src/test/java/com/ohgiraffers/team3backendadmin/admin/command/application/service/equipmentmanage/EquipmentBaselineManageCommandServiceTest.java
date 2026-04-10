package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.equipmentmanage.EquipmentBaselineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.equipmentmanage.EquipmentBaselineUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentBaseline;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentBaselineRepository;
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
class EquipmentBaselineManageCommandServiceTest {

    @Mock
    private EquipmentBaselineRepository equipmentBaselineRepository;

    @Mock
    private EquipmentReferenceSnapshotCommandService equipmentReferenceSnapshotCommandService;

    @InjectMocks
    private EquipmentBaselineManageCommandService equipmentBaselineManageCommandService;

    private EquipmentBaseline equipmentBaseline;
    private EquipmentBaselineUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        equipmentBaseline = EquipmentBaseline.builder()
            .equipmentBaselineId(6001L)
            .equipmentId(4001L)
            .equipmentAgingParamId(5001L)
            .build();

        updateRequest = EquipmentBaselineUpdateRequest.builder()
            .equipmentStandardPerformanceRate(BigDecimal.valueOf(96.0))
            .equipmentBaselineErrorRate(BigDecimal.valueOf(1.5))
            .equipmentEtaMaint(BigDecimal.valueOf(11.0))
            .equipmentIdx(BigDecimal.valueOf(90.0))
            .equipmentBaselineCalculatedAt(LocalDateTime.of(2026, 4, 1, 11, 0))
            .build();
    }

    @Test
    @DisplayName("Update equipment baseline success")
    void updateEquipmentBaseline_success() {
        when(equipmentBaselineRepository.findById(6001L)).thenReturn(Optional.of(equipmentBaseline));

        EquipmentBaselineUpdateResponse response =
            equipmentBaselineManageCommandService.updateEquipmentBaseline(6001L, updateRequest);

        assertEquals(6001L, response.getEquipmentBaselineId());
        assertEquals(4001L, response.getEquipmentId());
        assertEquals(5001L, response.getEquipmentAgingParamId());
        assertEquals(BigDecimal.valueOf(96.0), response.getEquipmentStandardPerformanceRate());
        assertEquals(BigDecimal.valueOf(1.5), response.getEquipmentBaselineErrorRate());
        assertEquals(BigDecimal.valueOf(11.0), response.getEquipmentEtaMaint());
        assertEquals(BigDecimal.valueOf(90.0), response.getEquipmentIdx());
    }

    @Test
    @DisplayName("Update equipment baseline failure when not found")
    void updateEquipmentBaseline_whenNotFound_thenThrow() {
        when(equipmentBaselineRepository.findById(9999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> equipmentBaselineManageCommandService.updateEquipmentBaseline(9999L, updateRequest)
        );

        assertEquals(ErrorCode.EQUIPMENT_BASELINE_NOT_FOUND, exception.getErrorCode());
    }
}
