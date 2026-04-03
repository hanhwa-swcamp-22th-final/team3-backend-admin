package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentCreateResponse {
  private Long equipmentId;
  private String equipmentCode;
  private String equipmentName;
  private EquipmentStatus equipmentStatus;
  private EquipmentGrade equipmentGrade;
}
