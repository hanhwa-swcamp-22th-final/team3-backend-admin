package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentProcessTest {

  private EquipmentProcess equipmentProcess;

  @BeforeEach
  void setUp() {
    equipmentProcess = EquipmentProcess.builder()
        .equipmentProcessId(2001L)
        .factoryLineId(1001L)
        .equipmentProcessCode("PROC-001")
        .equipmentProcessName("Mixing Process")
        .build();
  }

  @Test
  @DisplayName("Update equipment process info success: line id, code, and name are updated")
  void updateInfo_success(){

    equipmentProcess.updateInfo(9999L,"PROC-999","Fixed Process");

    assertEquals(9999L,equipmentProcess.getFactoryLineId());
    assertEquals("PROC-999",equipmentProcess.getEquipmentProcessCode());
    assertEquals("Fixed Process",equipmentProcess.getEquipmentProcessName());

  }

  @Test
  @DisplayName("Update equipment process info failure: throw exception when factory line id is null")
  void updateInfo_whenFactoryLineIdIsNull_thenThrow(){

    assertThrows(IllegalArgumentException.class,
        ()-> {
      equipmentProcess.updateInfo(null,"PROC-999","Fixed Process");
        });

  }

  @Test
  @DisplayName("Update equipment process info failure: throw exception when code is blank")
  void updateInfo_whenCodeIsBlank_thenThrow(){
    assertThrows(IllegalArgumentException.class,
        ()-> {
          equipmentProcess.updateInfo(9999L," ","Fixed Process");
        });

  }

  @Test
  @DisplayName("Update equipment process info failure: throw exception when name is blank")
  void updateInfo_whenNameIsBlank_thenThrow(){
    assertThrows(IllegalArgumentException.class,
        ()-> {
          equipmentProcess.updateInfo(9999L,"PROC-999"," ");
        });
  }

  @Test
  @DisplayName("Soft delete equipment process success: isDeleted is changed to true")
  void softDelete_success(){
    equipmentProcess.softDelete();

    assertTrue(equipmentProcess.getIsDeleted());
  }

}