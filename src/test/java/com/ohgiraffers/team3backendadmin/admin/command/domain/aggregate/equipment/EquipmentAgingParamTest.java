package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EquipmentAgingParamTest {

    private EquipmentAgingParam equipmentAgingParam;

    @BeforeEach
    void setUp() {
        equipmentAgingParam = EquipmentAgingParam.builder()
            .equipmentAgingParamId(5001L)
            .equipmentId(4001L)
            .equipmentWarrantyMonth(24)
            .equipmentDesignLifeMonths(120)
            .equipmentWearCoefficient(BigDecimal.valueOf(0.75))
            .build();
    }

    @Test
    @DisplayName("Update equipment aging param success: warranty, design life, and wear coefficient are updated")
    void update_success() {
        equipmentAgingParam.update(36, 180, BigDecimal.valueOf(0.85));

        assertEquals(36, equipmentAgingParam.getEquipmentWarrantyMonth());
        assertEquals(180, equipmentAgingParam.getEquipmentDesignLifeMonths());
        assertEquals(BigDecimal.valueOf(0.85), equipmentAgingParam.getEquipmentWearCoefficient());
    }

    @Test
    @DisplayName("Update equipment aging param failure: throw exception when warranty month is null")
    void update_whenWarrantyMonthIsNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            equipmentAgingParam.update(null, 180, BigDecimal.valueOf(0.85))
        );
    }

    @Test
    @DisplayName("Update equipment aging param failure: throw exception when warranty month is negative")
    void update_whenWarrantyMonthIsNegative_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            equipmentAgingParam.update(-1, 180, BigDecimal.valueOf(0.85))
        );
    }

    @Test
    @DisplayName("Update equipment aging param failure: throw exception when design life months is null")
    void update_whenDesignLifeMonthsIsNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            equipmentAgingParam.update(36, null, BigDecimal.valueOf(0.85))
        );
    }

    @Test
    @DisplayName("Update equipment aging param failure: throw exception when design life months is negative")
    void update_whenDesignLifeMonthsIsNegative_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            equipmentAgingParam.update(36, -1, BigDecimal.valueOf(0.85))
        );
    }

    @Test
    @DisplayName("Update equipment aging param failure: throw exception when wear coefficient is null")
    void update_whenWearCoefficientIsNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            equipmentAgingParam.update(36, 180, null)
        );
    }

    @Test
    @DisplayName("Update equipment aging param failure: throw exception when wear coefficient is negative")
    void update_whenWearCoefficientIsNegative_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            equipmentAgingParam.update(36, 180, BigDecimal.valueOf(-0.1))
        );
    }
}
