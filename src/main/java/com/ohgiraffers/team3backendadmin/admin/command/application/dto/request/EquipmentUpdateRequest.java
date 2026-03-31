package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentUpdateRequest {
  private Long equipmentProcessId;
  private Long environmentStandardId;
  private String equipmentCode;
  private String equipmentName;
  private EquipmentStatus equipmentStatus;
  private EquipmentGrade equipmentGrade;
  private String equipmentDescription;
  private Integer equipmentWarrantyMonth;
  private Integer equipmentDesignLifeMonths;
  private Double equipmentWearCoefficient;

}