package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance;

import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaintenanceItemStandardTest {

    private MaintenanceItemStandard maintenanceItemStandard;

    @BeforeEach
    void setUp() {
        maintenanceItemStandard = MaintenanceItemStandard.builder()
            .maintenanceItemStandardId(1001L)
            .maintenanceItem("Bearing Inspection")
            .maintenanceWeight(BigDecimal.valueOf(1.5))
            .maintenanceScoreMax(BigDecimal.valueOf(10.0))
            .build();
    }

    @Test
    @DisplayName("Update maintenance item standard success: fields are updated correctly")
    void updateInfo_success() {
        maintenanceItemStandard.updateInfo(
            "Motor Check",
            BigDecimal.valueOf(2.0),
            BigDecimal.valueOf(12.5)
        );

        assertEquals("Motor Check", maintenanceItemStandard.getMaintenanceItem());
        assertEquals(BigDecimal.valueOf(2.0), maintenanceItemStandard.getMaintenanceWeight());
        assertEquals(BigDecimal.valueOf(12.5), maintenanceItemStandard.getMaintenanceScoreMax());
    }

    @Test
    @DisplayName("Update maintenance item standard failure: throw exception when maintenance item is blank")
    void updateInfo_whenMaintenanceItemIsBlank_thenThrow() {
        assertThrows(BusinessException.class, () ->
            maintenanceItemStandard.updateInfo(
                " ",
                BigDecimal.valueOf(2.0),
                BigDecimal.valueOf(12.5)
            )
        );
    }

    @Test
    @DisplayName("Update maintenance item standard failure: throw exception when maintenance weight is null")
    void updateInfo_whenMaintenanceWeightIsNull_thenThrow() {
        assertThrows(BusinessException.class, () ->
            maintenanceItemStandard.updateInfo(
                "Motor Check",
                null,
                BigDecimal.valueOf(12.5)
            )
        );
    }

    @Test
    @DisplayName("Update maintenance item standard failure: throw exception when maintenance weight is negative")
    void updateInfo_whenMaintenanceWeightIsNegative_thenThrow() {
        assertThrows(BusinessException.class, () ->
            maintenanceItemStandard.updateInfo(
                "Motor Check",
                BigDecimal.valueOf(-1.0),
                BigDecimal.valueOf(12.5)
            )
        );
    }

    @Test
    @DisplayName("Update maintenance item standard failure: throw exception when maintenance score max is null")
    void updateInfo_whenMaintenanceScoreMaxIsNull_thenThrow() {
        assertThrows(BusinessException.class, () ->
            maintenanceItemStandard.updateInfo(
                "Motor Check",
                BigDecimal.valueOf(2.0),
                null
            )
        );
    }

    @Test
    @DisplayName("Update maintenance item standard failure: throw exception when maintenance score max is negative")
    void updateInfo_whenMaintenanceScoreMaxIsNegative_thenThrow() {
        assertThrows(BusinessException.class, () ->
            maintenanceItemStandard.updateInfo(
                "Motor Check",
                BigDecimal.valueOf(2.0),
                BigDecimal.valueOf(-1.0)
            )
        );
    }
}
