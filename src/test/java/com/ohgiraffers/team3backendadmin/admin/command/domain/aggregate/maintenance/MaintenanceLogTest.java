package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance;

import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaintenanceLogTest {

    private MaintenanceLog maintenanceLog;

    @BeforeEach
    void setUp() {
        maintenanceLog = MaintenanceLog.builder()
            .maintenanceLogId(2001L)
            .equipmentId(3001L)
            .maintenanceItemStandardId(1001L)
            .maintenanceType(MaintenanceType.REGULAR)
            .maintenanceDate(LocalDate.of(2026, 3, 31))
            .maintenanceScore(BigDecimal.valueOf(8.5))
            .etaMaintDelta(BigDecimal.valueOf(1.2))
            .maintenanceResult(MaintenanceResult.NORMAL)
            .build();
    }

    @Test
    @DisplayName("Update maintenance log success: fields are updated correctly")
    void updateInfo_success() {
        maintenanceLog.updateInfo(
            3002L,
            1002L,
            MaintenanceType.IRREGULAR,
            LocalDate.of(2026, 4, 1),
            BigDecimal.valueOf(7.0),
            BigDecimal.valueOf(0.8),
            MaintenanceResult.REPAIR_REQUIRED
        );

        assertEquals(3002L, maintenanceLog.getEquipmentId());
        assertEquals(1002L, maintenanceLog.getMaintenanceItemStandardId());
        assertEquals(MaintenanceType.IRREGULAR, maintenanceLog.getMaintenanceType());
        assertEquals(LocalDate.of(2026, 4, 1), maintenanceLog.getMaintenanceDate());
        assertEquals(BigDecimal.valueOf(7.0), maintenanceLog.getMaintenanceScore());
        assertEquals(BigDecimal.valueOf(0.8), maintenanceLog.getEtaMaintDelta());
        assertEquals(MaintenanceResult.REPAIR_REQUIRED, maintenanceLog.getMaintenanceResult());
    }

    @Test
    @DisplayName("Update maintenance log success: nullable score fields can be cleared")
    void updateInfo_whenOptionalFieldsAreNull_thenSuccess() {
        maintenanceLog.updateInfo(
            3002L,
            1002L,
            MaintenanceType.IRREGULAR,
            LocalDate.of(2026, 4, 1),
            null,
            null,
            MaintenanceResult.REPAIR_COMPLETED
        );

        assertEquals(3002L, maintenanceLog.getEquipmentId());
        assertEquals(1002L, maintenanceLog.getMaintenanceItemStandardId());
        assertEquals(MaintenanceType.IRREGULAR, maintenanceLog.getMaintenanceType());
        assertEquals(LocalDate.of(2026, 4, 1), maintenanceLog.getMaintenanceDate());
        assertEquals(null, maintenanceLog.getMaintenanceScore());
        assertEquals(null, maintenanceLog.getEtaMaintDelta());
        assertEquals(MaintenanceResult.REPAIR_COMPLETED, maintenanceLog.getMaintenanceResult());
    }

    @Test
    @DisplayName("Update maintenance log failure: throw exception when equipment id is null")
    void updateInfo_whenEquipmentIdIsNull_thenThrow() {
        assertThrows(BusinessException.class, () ->
            maintenanceLog.updateInfo(
                null,
                1002L,
                MaintenanceType.IRREGULAR,
                LocalDate.of(2026, 4, 1),
                BigDecimal.valueOf(7.0),
                BigDecimal.valueOf(0.8),
                MaintenanceResult.REPAIR_REQUIRED
            )
        );
    }

    @Test
    @DisplayName("Update maintenance log failure: throw exception when maintenance item standard id is null")
    void updateInfo_whenMaintenanceItemStandardIdIsNull_thenThrow() {
        assertThrows(BusinessException.class, () ->
            maintenanceLog.updateInfo(
                3002L,
                null,
                MaintenanceType.IRREGULAR,
                LocalDate.of(2026, 4, 1),
                BigDecimal.valueOf(7.0),
                BigDecimal.valueOf(0.8),
                MaintenanceResult.REPAIR_REQUIRED
            )
        );
    }

    @Test
    @DisplayName("Update maintenance log failure: throw exception when maintenance type is null")
    void updateInfo_whenMaintenanceTypeIsNull_thenThrow() {
        assertThrows(BusinessException.class, () ->
            maintenanceLog.updateInfo(
                3002L,
                1002L,
                null,
                LocalDate.of(2026, 4, 1),
                BigDecimal.valueOf(7.0),
                BigDecimal.valueOf(0.8),
                MaintenanceResult.REPAIR_REQUIRED
            )
        );
    }

    @Test
    @DisplayName("Update maintenance log failure: throw exception when maintenance date is null")
    void updateInfo_whenMaintenanceDateIsNull_thenThrow() {
        assertThrows(BusinessException.class, () ->
            maintenanceLog.updateInfo(
                3002L,
                1002L,
                MaintenanceType.IRREGULAR,
                null,
                BigDecimal.valueOf(7.0),
                BigDecimal.valueOf(0.8),
                MaintenanceResult.REPAIR_REQUIRED
            )
        );
    }

    @Test
    @DisplayName("Update maintenance log failure: throw exception when maintenance score is negative")
    void updateInfo_whenMaintenanceScoreIsNegative_thenThrow() {
        assertThrows(BusinessException.class, () ->
            maintenanceLog.updateInfo(
                3002L,
                1002L,
                MaintenanceType.IRREGULAR,
                LocalDate.of(2026, 4, 1),
                BigDecimal.valueOf(-1.0),
                BigDecimal.valueOf(0.8),
                MaintenanceResult.REPAIR_REQUIRED
            )
        );
    }

    @Test
    @DisplayName("Update maintenance log failure: throw exception when eta maintenance delta is negative")
    void updateInfo_whenEtaMaintDeltaIsNegative_thenThrow() {
        assertThrows(BusinessException.class, () ->
            maintenanceLog.updateInfo(
                3002L,
                1002L,
                MaintenanceType.IRREGULAR,
                LocalDate.of(2026, 4, 1),
                BigDecimal.valueOf(7.0),
                BigDecimal.valueOf(-0.1),
                MaintenanceResult.REPAIR_REQUIRED
            )
        );
    }

    @Test
    @DisplayName("Update maintenance log failure: throw exception when maintenance result is null")
    void updateInfo_whenMaintenanceResultIsNull_thenThrow() {
        assertThrows(BusinessException.class, () ->
            maintenanceLog.updateInfo(
                3002L,
                1002L,
                MaintenanceType.IRREGULAR,
                LocalDate.of(2026, 4, 1),
                BigDecimal.valueOf(7.0),
                BigDecimal.valueOf(0.8),
                null
            )
        );
    }
}
