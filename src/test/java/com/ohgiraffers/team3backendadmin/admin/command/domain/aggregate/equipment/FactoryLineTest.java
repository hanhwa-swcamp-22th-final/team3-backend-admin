package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryLineTest {

  private FactoryLine factoryLine;

  @BeforeEach
  void setUp() {
    factoryLine = FactoryLine.builder()
        .factoryLineId(1001L)
        .factoryLineCode("LINE-001")
        .factoryLineName("Main Line")
        .build();
  }

  @Test
  @DisplayName("Update factory line info success: code and name are updated")
  void updateInfo_success(){
    factoryLine.updateInfo("LINE-999","Fixed Line");

    assertEquals("LINE-999",factoryLine.getFactoryLineCode());
    assertEquals("Fixed Line", factoryLine.getFactoryLineName());
  }

  @Test
  @DisplayName("Update factory line info failure: throw exception when code is blank")
  void updateInfo_whenCodeIsBlank_thenThrow(){

    assertThrows(IllegalArgumentException.class,() -> {
      factoryLine.updateInfo(" ","Fixed Line");
    });
  }

  @Test
  @DisplayName("Update factory line info failure: throw exception when name is blank")
  void updateInfo_whenNameIsBlank_thenThrow(){
    assertThrows(IllegalArgumentException.class,() -> {
      factoryLine.updateInfo("LINE-999"," ");
    });
  }

  @Test
  @DisplayName("Soft delete factory line success: isDeleted is changed to true")
  void softDelete_success(){

    factoryLine.softDelete();
    assertTrue(factoryLine.getIsDeleted());
  }


}