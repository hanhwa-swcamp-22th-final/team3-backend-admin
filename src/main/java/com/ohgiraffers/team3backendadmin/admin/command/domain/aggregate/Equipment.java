package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipment")
@Getter
@NoArgsConstructor
public class Equipment {

  @Id
  @Column(name = "equipment_id")
  private Long equipmentId;

  @Column(name = "equipment_process_id", nullable = false)
  private Long equipmentProcessId;

  @Column(name = "environment_standard_id", nullable = false)
  private Long environmentStandardId;

  @Column(name = "equipment_code", nullable = false, unique = true)
  private String equipmentCode;

  @Column(name = "equipment_name", nullable = false)
  private String equipmentName;

  @Enumerated(EnumType.STRING)
  @Column(name = "equipment_status", nullable = false)
  private EquipmentStatus equipmentStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "equipment_grade", nullable = false)
  private EquipmentGrade equipmentGrade;

  @Column(name = "equipment_description")
  private String equipmentDescription;

  public Equipment(
    Long equipmentId,
    Long equipmentProcessId,
    Long environmentStandardId,
    String equipmentCode,
    String equipmentName,
    EquipmentStatus equipmentStatus,
    EquipmentGrade equipmentGrade,
    String equipmentDescription)
  {
    this.equipmentId = equipmentId;
    this.equipmentProcessId = equipmentProcessId;
    this.environmentStandardId = environmentStandardId;
    this.equipmentCode = equipmentCode;
    this.equipmentName = equipmentName;
    this.equipmentStatus = equipmentStatus;
    this.equipmentGrade = equipmentGrade;
    this.equipmentDescription = equipmentDescription;
  }

  public void changeStatus(EquipmentStatus equipmentStatus) {
    if (equipmentStatus == null) {
      throw new IllegalArgumentException("설비 상태는 null일 수 없습니다");
    }

    this.equipmentStatus = equipmentStatus;

  }

  public void updateInfo(String equipmentName,
                         EquipmentGrade equipmentGrade,
                         String equipmentDescription) {

    if (equipmentName == null || equipmentGrade == null){
      throw new IllegalArgumentException("설비 정보 변경 값은 null 일 수 없습니다");
    }

    this.equipmentName = equipmentName;
    this.equipmentGrade = equipmentGrade;
    this.equipmentDescription = equipmentDescription;
  }
}
