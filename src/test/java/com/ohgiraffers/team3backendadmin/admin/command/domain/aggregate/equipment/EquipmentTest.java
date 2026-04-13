package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EquipmentTest {

    private Equipment equipment;

    @BeforeEach
    void setUp() {
        equipment = Equipment.builder()
            .equipmentId(260326163530023L)
            .equipmentProcessId(1L)
            .environmentStandardId(1L)
            .equipmentCode("EQ-001")
            .equipmentName("Drying Equipment")
            .equipmentStatus(EquipmentStatus.OPERATING)
            .equipmentGrade(EquipmentGrade.A)
            .equipmentDescription("Initial description")
            .build();
    }

    @Test
    @DisplayName("Create equipment success: constructor values are applied correctly")
    void constructor_success() {
        assertEquals(260326163530023L, equipment.getEquipmentId());
        assertEquals(1L, equipment.getEquipmentProcessId());
        assertEquals(1L, equipment.getEnvironmentStandardId());
        assertEquals("EQ-001", equipment.getEquipmentCode());
        assertEquals("Drying Equipment", equipment.getEquipmentName());
        assertEquals(EquipmentStatus.OPERATING, equipment.getEquipmentStatus());
        assertEquals(EquipmentGrade.A, equipment.getEquipmentGrade());
        assertEquals("Initial description", equipment.getEquipmentDescription());
        assertFalse(equipment.getIsDeleted());
    }

    @Test
    @DisplayName("Change equipment status success: status is updated")
    void changeStatus_success() {
        equipment.changeStatus(EquipmentStatus.STOPPED);

        assertEquals(EquipmentStatus.STOPPED, equipment.getEquipmentStatus());
    }

    @Test
    @DisplayName("Change equipment status failure: throw exception when status is null")
    void changeStatus_whenStatusIsNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () -> equipment.changeStatus(null));
    }

    @Test
    @DisplayName("Update equipment info success: name, grade, and description are updated")
    void updateInfo_success() {
        equipment.updateInfo("Printing Equipment", EquipmentGrade.S, "Updated description");

        assertEquals("Printing Equipment", equipment.getEquipmentName());
        assertEquals(EquipmentGrade.S, equipment.getEquipmentGrade());
        assertEquals("Updated description", equipment.getEquipmentDescription());
    }

    @Test
    @DisplayName("Update equipment info failure: throw exception when equipment name is blank")
    void updateInfo_whenEquipmentNameIsBlank_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            equipment.updateInfo(" ", EquipmentGrade.S, "Updated description")
        );
    }

    @Test
    @DisplayName("Update equipment info failure: throw exception when equipment grade is null")
    void updateInfo_whenEquipmentGradeIsNull_thenThrow() {
        assertThrows(IllegalArgumentException.class, () ->
            equipment.updateInfo("Printing Equipment", null, "Updated description")
        );
    }

    @Test
    @DisplayName("Soft delete equipment success: isDeleted is changed to true")
    void softDelete_success() {
        equipment.softDelete();

        assertTrue(equipment.getIsDeleted());
    }
}
