package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceLogCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceLogUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceLogCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceLogUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceItemStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceLog;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceResult;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceType;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.MaintenanceItemStandardRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.MaintenanceLogRepository;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MaintenanceLogManageCommandServiceTest {

    @Mock
    private MaintenanceLogRepository maintenanceLogRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private MaintenanceItemStandardRepository maintenanceItemStandardRepository;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private MaintenanceLogManageCommandService maintenanceLogManageCommandService;

    private Equipment equipment;
    private MaintenanceItemStandard maintenanceItemStandard;
    private MaintenanceLog maintenanceLog;
    private MaintenanceLogCreateRequest createRequest;
    private MaintenanceLogUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        equipment = Equipment.builder()
            .equipmentId(4001L)
            .equipmentProcessId(2001L)
            .environmentStandardId(3001L)
            .equipmentCode("EQ-001")
            .equipmentName("Drying Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.A)
            .equipmentDescription("Maintenance equipment")
            .build();

        maintenanceItemStandard = MaintenanceItemStandard.builder()
            .maintenanceItemStandardId(7001L)
            .maintenanceItem("Bearing Inspection")
            .maintenanceWeight(BigDecimal.valueOf(2))
            .maintenanceScoreMax(BigDecimal.valueOf(10))
            .build();

        maintenanceLog = MaintenanceLog.builder()
            .maintenanceLogId(8001L)
            .equipmentId(4001L)
            .maintenanceItemStandardId(7001L)
            .maintenanceType(MaintenanceType.REGULAR)
            .maintenanceDate(LocalDate.of(2026, 4, 1))
            .maintenanceScore(BigDecimal.valueOf(9))
            .etaMaintDelta(BigDecimal.valueOf(1))
            .maintenanceResult(MaintenanceResult.NORMAL)
            .build();

        createRequest = MaintenanceLogCreateRequest.builder()
            .equipmentId(4001L)
            .maintenanceItemStandardId(7001L)
            .maintenanceType(MaintenanceType.REGULAR)
            .maintenanceDate(LocalDate.of(2026, 4, 1))
            .maintenanceScore(BigDecimal.valueOf(9))
            .etaMaintDelta(BigDecimal.valueOf(1))
            .maintenanceResult(MaintenanceResult.NORMAL)
            .build();

        updateRequest = MaintenanceLogUpdateRequest.builder()
            .equipmentId(4001L)
            .maintenanceItemStandardId(7001L)
            .maintenanceType(MaintenanceType.IRREGULAR)
            .maintenanceDate(LocalDate.of(2026, 4, 2))
            .maintenanceScore(BigDecimal.valueOf(8))
            .etaMaintDelta(BigDecimal.valueOf(2))
            .maintenanceResult(MaintenanceResult.REPAIR_REQUIRED)
            .build();
    }

    @Test
    @DisplayName("Create maintenance log success")
    void createMaintenanceLog_success() {
        when(equipmentRepository.findById(4001L)).thenReturn(Optional.of(equipment));
        when(maintenanceItemStandardRepository.findById(7001L)).thenReturn(Optional.of(maintenanceItemStandard));
        when(idGenerator.generate()).thenReturn(8001L);
        when(maintenanceLogRepository.save(any(MaintenanceLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MaintenanceLogCreateResponse response = maintenanceLogManageCommandService.createMaintenanceLog(createRequest);

        ArgumentCaptor<MaintenanceLog> captor = ArgumentCaptor.forClass(MaintenanceLog.class);
        verify(maintenanceLogRepository).save(captor.capture());

        MaintenanceLog savedMaintenanceLog = captor.getValue();
        assertEquals(8001L, savedMaintenanceLog.getMaintenanceLogId());
        assertEquals(4001L, savedMaintenanceLog.getEquipmentId());
        assertEquals(7001L, savedMaintenanceLog.getMaintenanceItemStandardId());
        assertEquals(MaintenanceType.REGULAR, savedMaintenanceLog.getMaintenanceType());
        assertEquals(MaintenanceResult.NORMAL, savedMaintenanceLog.getMaintenanceResult());
        assertEquals(8001L, response.getMaintenanceLogId());
        assertEquals(4001L, response.getEquipmentId());
    }

    @Test
    @DisplayName("Create maintenance log failure when equipment not found")
    void createMaintenanceLog_whenEquipmentNotFound_thenThrow() {
        when(equipmentRepository.findById(4001L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> maintenanceLogManageCommandService.createMaintenanceLog(createRequest));

        assertEquals(ErrorCode.EQUIPMENT_NOT_FOUND, exception.getErrorCode());
        verify(maintenanceLogRepository, never()).save(any(MaintenanceLog.class));
    }

    @Test
    @DisplayName("Create maintenance log failure when maintenance item standard not found")
    void createMaintenanceLog_whenMaintenanceItemStandardNotFound_thenThrow() {
        when(equipmentRepository.findById(4001L)).thenReturn(Optional.of(equipment));
        when(maintenanceItemStandardRepository.findById(7001L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> maintenanceLogManageCommandService.createMaintenanceLog(createRequest));

        assertEquals(ErrorCode.MAINTENANCE_ITEM_STANDARD_NOT_FOUND, exception.getErrorCode());
        verify(maintenanceLogRepository, never()).save(any(MaintenanceLog.class));
    }

    @Test
    @DisplayName("Update maintenance log success")
    void updateMaintenanceLog_success() {
        when(maintenanceLogRepository.findById(8001L)).thenReturn(Optional.of(maintenanceLog));
        when(equipmentRepository.findById(4001L)).thenReturn(Optional.of(equipment));
        when(maintenanceItemStandardRepository.findById(7001L)).thenReturn(Optional.of(maintenanceItemStandard));

        MaintenanceLogUpdateResponse response = maintenanceLogManageCommandService.updateMaintenanceLog(8001L, updateRequest);

        assertEquals(MaintenanceType.IRREGULAR, maintenanceLog.getMaintenanceType());
        assertEquals(MaintenanceResult.REPAIR_REQUIRED, maintenanceLog.getMaintenanceResult());
        assertEquals(BigDecimal.valueOf(8), maintenanceLog.getMaintenanceScore());
        assertEquals(8001L, response.getMaintenanceLogId());
        assertEquals(MaintenanceType.IRREGULAR, response.getMaintenanceType());
    }

    @Test
    @DisplayName("Update maintenance log failure when not found")
    void updateMaintenanceLog_whenNotFound_thenThrow() {
        when(maintenanceLogRepository.findById(9999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> maintenanceLogManageCommandService.updateMaintenanceLog(9999L, updateRequest));

        assertEquals(ErrorCode.MAINTENANCE_LOG_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Delete maintenance log success")
    void deleteMaintenanceLog_success() {
        when(maintenanceLogRepository.findById(8001L)).thenReturn(Optional.of(maintenanceLog));

        MaintenanceLogUpdateResponse response = maintenanceLogManageCommandService.deleteMaintenanceLog(8001L);

        verify(maintenanceLogRepository).delete(maintenanceLog);
        assertEquals(8001L, response.getMaintenanceLogId());
        assertEquals(4001L, response.getEquipmentId());
    }

    @Test
    @DisplayName("Delete maintenance log failure when not found")
    void deleteMaintenanceLog_whenNotFound_thenThrow() {
        when(maintenanceLogRepository.findById(9999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> maintenanceLogManageCommandService.deleteMaintenanceLog(9999L));

        assertEquals(ErrorCode.MAINTENANCE_LOG_NOT_FOUND, exception.getErrorCode());
    }
}