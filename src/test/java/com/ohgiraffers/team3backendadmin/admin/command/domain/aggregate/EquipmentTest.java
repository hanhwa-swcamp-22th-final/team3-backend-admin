package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import com.ohgiraffers.team3backendadmin.common.idgenerator.TimeBasedIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EquipmentTest {

  TimeBasedIdGenerator idGenerator = new TimeBasedIdGenerator();

  @Test
  @DisplayName("설비 생성 성공: 생성자 입력값이 정상 반영된다")
  void constructor_success() {

    Equipment equipment = new Equipment(
        260326163530023123L,
        1L,
        1L,
        "EQ-001",
        "건조 설비",
        EquipmentStatus.OPERATING,
        EquipmentGrade.A,
        "초기 설명");

    assertEquals(1L, equipment.getEquipmentProcessId());
    assertEquals(1L, equipment.getEnvironmentStandardId());
    assertEquals("EQ-001", equipment.getEquipmentCode());
    assertEquals("건조 설비", equipment.getEquipmentName());
    assertEquals(EquipmentStatus.OPERATING, equipment.getEquipmentStatus());
    assertEquals(EquipmentGrade.A, equipment.getEquipmentGrade());
    assertEquals("초기 설명", equipment.getEquipmentDescription());
  }

  @Test
  @DisplayName("설비 상태 변경 성공: 상태가 변경된다")
  void changeStatus_success() {
    Equipment equipment = new Equipment(
        260326163530023123L,
        1L,
        1L,
        "EQ-001",
        "건조 설비",
        EquipmentStatus.OPERATING,
        EquipmentGrade.A,
        "초기 설명");

    equipment.changeStatus(EquipmentStatus.STOPPED);

    assertEquals(EquipmentStatus.STOPPED, equipment.getEquipmentStatus());

  }

  @Test
  @DisplayName("설비 상태 변경 실패: 상태가 null이면 예외가 발생한다")
  void changeStatus_whenStatusIsNull_thenThrow() {
    Equipment equipment = new Equipment(
        260326163530023123L,
        1L,
        1L,
        "EQ-001",
        "건조 설비",
        EquipmentStatus.OPERATING,
        EquipmentGrade.A,
        "초기 설명"
    );

    assertThrows(IllegalArgumentException.class, () ->
        equipment.changeStatus(null)
    );
  }


  @Test
  @DisplayName("설비 정보 수정 성공: 이름, 등급, 설명이 변경된다")
  void updateInfo_success() {
    Equipment equipment = new Equipment(
        260326163530023123L,
        1L,
        1L,
        "EQ-001",
        "건조 설비",
        EquipmentStatus.OPERATING,
        EquipmentGrade.A,
        "초기 설명"
    );

    equipment.updateInfo("노칭 설비",EquipmentGrade.S,"수정 설명");
    assertEquals("노칭 설비", equipment.getEquipmentName());
    assertEquals(EquipmentGrade.S, equipment.getEquipmentGrade());
    assertEquals("수정 설명", equipment.getEquipmentDescription());

  }

  @Test
  @DisplayName("설비 정보 수정 실패: 설비명이 비어 있으면 예외가 발생한다")
  void updateInfo_whenEquipmentNameIsBlank_thenThrow() {
    Equipment equipment = new Equipment(
        260326163530023123L,
        1L,
        1L,
        "EQ-001",
        "건조 설비",
        EquipmentStatus.OPERATING,
        EquipmentGrade.A,
        "초기 설명"
    );

    assertThrows(IllegalArgumentException.class,()->{
      equipment.updateInfo(null,EquipmentGrade.S,"수정 설명");
    });
  }

  @Test
  @DisplayName("설비 정보 수정 실패: 설비 등급이 null이면 예외가 발생한다")
  void updateInfo_whenEquipmentGradeIsNull_thenThrow() {
    Equipment equipment = new Equipment(
        260326163530023123L,
        1L,
        1L,
        "EQ-001",
        "건조 설비",
        EquipmentStatus.OPERATING,
        EquipmentGrade.A,
        "초기 설명"
    );

    assertThrows(IllegalArgumentException.class,()->{
      equipment.updateInfo("노칭 설비",null,"수정 설명");
    });
  }

}
