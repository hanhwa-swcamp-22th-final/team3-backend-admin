package com.ohgiraffers.team3backendadmin.admin.command.application.dto.request;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentUpdateRequest {
  @NotNull(message = "공정 ID는 필수입니다.")
  private Long equipmentProcessId;

  @NotNull(message = "환경 기준 ID는 필수입니다.")
  private Long environmentStandardId;

  @NotBlank(message = "설비 코드는 필수입니다.")
  private String equipmentCode;

  @NotBlank(message = "설비명은 필수입니다.")
  private String equipmentName;

  @NotNull(message = "설비 상태는 필수입니다.")
  private EquipmentStatus equipmentStatus;

  @NotNull(message = "설비 등급은 필수입니다.")
  private EquipmentGrade equipmentGrade;
  private String equipmentDescription;

  @NotNull(message = "보증 개월 수는 필수입니다.")
  @PositiveOrZero(message = "보증 개월 수는 음수일 수 없습니다.")
  private Integer equipmentWarrantyMonth;

  @NotNull(message = "설계 수명 개월 수는 필수입니다.")
  @PositiveOrZero(message = "설계 수명 개월 수는 음수일 수 없습니다.")
  private Integer equipmentDesignLifeMonths;

  @NotNull(message = "마모 계수는 필수입니다.")
  @DecimalMin(value = "0.0", inclusive = true, message = "마모 계수는 음수일 수 없습니다.")
  private Double equipmentWearCoefficient;

}
