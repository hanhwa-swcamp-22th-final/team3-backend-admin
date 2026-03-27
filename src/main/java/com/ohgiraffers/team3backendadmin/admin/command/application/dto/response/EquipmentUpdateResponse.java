package com.ohgiraffers.team3backendadmin.admin.command.application.dto.response;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentStatus;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentUpdateResponse {
  private Long equipmentId;
  private String equipmentCode;
  private String equipmentName;
  private EquipmentStatus equipmentStatus;
  private EquipmentGrade equipmentGrade;
}
