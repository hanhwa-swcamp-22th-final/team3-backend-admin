package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EquipmentBaselineTest {

    private EquipmentBaseline equipmentBaseline;
    private LocalDateTime calculatedAt;

    @BeforeEach
    void setUp() {
        calculatedAt = LocalDateTime.of(2026, 4, 1, 10, 0);

        equipmentBaseline = EquipmentBaseline.builder()
            .equipmentBaselineId(6001L)
            .equipmentId(4001L)
            .equipmentAgingParamId(5001L)
            .equipmentStandardPerformanceRate(BigDecimal.valueOf(95.0))
            .equipmentBaselineErrorRate(BigDecimal.valueOf(2.5))
            .equipmentEtaMaint(BigDecimal.valueOf(12.0))
            .equipmentIdx(BigDecimal.valueOf(88.0))
            .equipmentBaselineCalculatedAt(calculatedAt)
            .build();
    }

    @Test
    @DisplayName("Update equipment baseline success: baseline metrics are updated")
    void update_success() {
        LocalDateTime updatedCalculatedAt = LocalDateTime.of(2026, 4, 2, 8, 30);

        equipmentBaseline.update(
            BigDecimal.valueOf(97.5),
            BigDecimal.valueOf(1.8),
            BigDecimal.valueOf(10.5),
            BigDecimal.valueOf(91.2),
            updatedCalculatedAt
        );

        assertEquals(BigDecimal.valueOf(97.5), equipmentBaseline.getEquipmentStandardPerformanceRate());
        assertEquals(BigDecimal.valueOf(1.8), equipmentBaseline.getEquipmentBaselineErrorRate());
        assertEquals(BigDecimal.valueOf(10.5), equipmentBaseline.getEquipmentEtaMaint());
        assertEquals(BigDecimal.valueOf(91.2), equipmentBaseline.getEquipmentIdx());
        assertEquals(updatedCalculatedAt, equipmentBaseline.getEquipmentBaselineCalculatedAt());
    }

    @Test
    @DisplayName("Update equipment baseline success: allow null calculated values before batch execution")
    void update_whenCalculatedValuesAreNull_thenAllow() {
        equipmentBaseline.update(null, null, null, null, null);

        assertNull(equipmentBaseline.getEquipmentStandardPerformanceRate());
        assertNull(equipmentBaseline.getEquipmentBaselineErrorRate());
        assertNull(equipmentBaseline.getEquipmentEtaMaint());
        assertNull(equipmentBaseline.getEquipmentIdx());
        assertNull(equipmentBaseline.getEquipmentBaselineCalculatedAt());
    }

    @Test
    @DisplayName("Update equipment baseline failure: throw exception when standard performance rate is negative")
    void update_whenStandardPerformanceRateIsNegative_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            equipmentBaseline.update(
                BigDecimal.valueOf(-1.0),
                BigDecimal.valueOf(1.8),
                BigDecimal.valueOf(10.5),
                BigDecimal.valueOf(91.2),
                LocalDateTime.of(2026, 4, 2, 8, 30)
            )
        );
    }

    @Test
    @DisplayName("Update equipment baseline failure: throw exception when baseline error rate is negative")
    void update_whenBaselineErrorRateIsNegative_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            equipmentBaseline.update(
                BigDecimal.valueOf(97.5),
                BigDecimal.valueOf(-0.1),
                BigDecimal.valueOf(10.5),
                BigDecimal.valueOf(91.2),
                LocalDateTime.of(2026, 4, 2, 8, 30)
            )
        );
    }

    @Test
    @DisplayName("Update equipment baseline failure: throw exception when eta maintenance is negative")
    void update_whenEtaMaintIsNegative_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            equipmentBaseline.update(
                BigDecimal.valueOf(97.5),
                BigDecimal.valueOf(1.8),
                BigDecimal.valueOf(-1.0),
                BigDecimal.valueOf(91.2),
                LocalDateTime.of(2026, 4, 2, 8, 30)
            )
        );
    }

    @Test
    @DisplayName("Update equipment baseline failure: throw exception when equipment index is negative")
    void update_whenEquipmentIdxIsNegative_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            equipmentBaseline.update(
                BigDecimal.valueOf(97.5),
                BigDecimal.valueOf(1.8),
                BigDecimal.valueOf(10.5),
                BigDecimal.valueOf(-1.0),
                LocalDateTime.of(2026, 4, 2, 8, 30)
            )
        );
    }

}
