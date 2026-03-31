package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceItemStandardCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceItemStandardUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceItemStandardCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceItemStandardUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceItemStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.MaintenanceItemStandardRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MaintenanceItemStandardManageCommandServiceTest {

    @Mock
    private MaintenanceItemStandardRepository maintenanceItemStandardRepository;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private MaintenanceItemStandardManageCommandService maintenanceItemStandardManageCommandService;

    private MaintenanceItemStandard maintenanceItemStandard;
    private MaintenanceItemStandardCreateRequest createRequest;
    private MaintenanceItemStandardUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        maintenanceItemStandard = MaintenanceItemStandard.builder()
            .maintenanceItemStandardId(7001L)
            .maintenanceItem("Bearing Inspection")
            .maintenanceWeight(BigDecimal.valueOf(2))
            .maintenanceScoreMax(BigDecimal.valueOf(10))
            .build();

        createRequest = MaintenanceItemStandardCreateRequest.builder()
            .maintenanceItem("Bearing Inspection")
            .maintenanceWeight(BigDecimal.valueOf(2))
            .maintenanceScoreMax(BigDecimal.valueOf(10))
            .build();

        updateRequest = MaintenanceItemStandardUpdateRequest.builder()
            .maintenanceItem("Motor Check")
            .maintenanceWeight(BigDecimal.valueOf(3))
            .maintenanceScoreMax(BigDecimal.valueOf(12))
            .build();
    }

    @Test
    @DisplayName("Create maintenance item standard success")
    void createMaintenanceItemStandard_success() {
        when(idGenerator.generate()).thenReturn(7001L);
        when(maintenanceItemStandardRepository.save(any(MaintenanceItemStandard.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MaintenanceItemStandardCreateResponse response = maintenanceItemStandardManageCommandService.createMaintenanceItemStandard(createRequest);

        ArgumentCaptor<MaintenanceItemStandard> captor = ArgumentCaptor.forClass(MaintenanceItemStandard.class);
        verify(maintenanceItemStandardRepository).save(captor.capture());

        MaintenanceItemStandard savedMaintenanceItemStandard = captor.getValue();
        assertEquals(7001L, savedMaintenanceItemStandard.getMaintenanceItemStandardId());
        assertEquals("Bearing Inspection", savedMaintenanceItemStandard.getMaintenanceItem());
        assertEquals(BigDecimal.valueOf(2), savedMaintenanceItemStandard.getMaintenanceWeight());
        assertEquals(BigDecimal.valueOf(10), savedMaintenanceItemStandard.getMaintenanceScoreMax());
        assertEquals(7001L, response.getMaintenanceItemStandardId());
        assertEquals("Bearing Inspection", response.getMaintenanceItem());
    }

    @Test
    @DisplayName("Update maintenance item standard success")
    void updateMaintenanceItemStandard_success() {
        when(maintenanceItemStandardRepository.findById(7001L)).thenReturn(Optional.of(maintenanceItemStandard));

        MaintenanceItemStandardUpdateResponse response = maintenanceItemStandardManageCommandService.updateMaintenanceItemStandard(7001L, updateRequest);

        assertEquals("Motor Check", maintenanceItemStandard.getMaintenanceItem());
        assertEquals(BigDecimal.valueOf(3), maintenanceItemStandard.getMaintenanceWeight());
        assertEquals(BigDecimal.valueOf(12), maintenanceItemStandard.getMaintenanceScoreMax());
        assertEquals(7001L, response.getMaintenanceItemStandardId());
        assertEquals("Motor Check", response.getMaintenanceItem());
    }

    @Test
    @DisplayName("Update maintenance item standard failure when not found")
    void updateMaintenanceItemStandard_whenNotFound_thenThrow() {
        when(maintenanceItemStandardRepository.findById(9999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> maintenanceItemStandardManageCommandService.updateMaintenanceItemStandard(9999L, updateRequest));

        assertEquals(ErrorCode.MAINTENANCE_ITEM_STANDARD_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Delete maintenance item standard success")
    void deleteMaintenanceItemStandard_success() {
        when(maintenanceItemStandardRepository.findById(7001L)).thenReturn(Optional.of(maintenanceItemStandard));

        MaintenanceItemStandardUpdateResponse response = maintenanceItemStandardManageCommandService.deleteMaintenanceItemStandard(7001L);

        verify(maintenanceItemStandardRepository).delete(maintenanceItemStandard);
        assertEquals(7001L, response.getMaintenanceItemStandardId());
        assertEquals("Bearing Inspection", response.getMaintenanceItem());
    }

    @Test
    @DisplayName("Delete maintenance item standard failure when not found")
    void deleteMaintenanceItemStandard_whenNotFound_thenThrow() {
        when(maintenanceItemStandardRepository.findById(9999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> maintenanceItemStandardManageCommandService.deleteMaintenanceItemStandard(9999L));

        assertEquals(ErrorCode.MAINTENANCE_ITEM_STANDARD_NOT_FOUND, exception.getErrorCode());
    }
}