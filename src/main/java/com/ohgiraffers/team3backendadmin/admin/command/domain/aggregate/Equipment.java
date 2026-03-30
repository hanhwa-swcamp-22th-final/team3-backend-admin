package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipment")
@EntityListeners(AuditingEntityListener.class)
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

  @Column(name = "equipment_install_date")
  private LocalDateTime equipmentInstallDate;

  @Column(name = "equipment_description")
  private String equipmentDescription;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private Long createdBy;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "updated_by")
  private Long updatedBy;

  @Builder
  public Equipment(Long equipmentId,
                   Long equipmentProcessId,
                   Long environmentStandardId,
                   String equipmentCode,
                   String equipmentName,
                   EquipmentStatus equipmentStatus,
                   EquipmentGrade equipmentGrade,
                   LocalDateTime equipmentInstallDate,
                   String equipmentDescription,
                   LocalDateTime createdAt,
                   Long createdBy,
                   LocalDateTime updatedAt,
                   Long updatedBy) {
    this.equipmentId = equipmentId;
    this.equipmentProcessId = equipmentProcessId;
    this.environmentStandardId = environmentStandardId;
    this.equipmentCode = equipmentCode;
    this.equipmentName = equipmentName;
    this.equipmentStatus = equipmentStatus;
    this.equipmentGrade = equipmentGrade;
    this.equipmentInstallDate = equipmentInstallDate;
    this.equipmentDescription = equipmentDescription;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.updatedAt = updatedAt;
    this.updatedBy = updatedBy;
  }

  public void changeStatus(EquipmentStatus equipmentStatus) {
    if (equipmentStatus == null) {
      throw new IllegalArgumentException("Equipment status cannot be null.");
    }

    this.equipmentStatus = equipmentStatus;
  }

  public void updateInfo(String equipmentName,
                         EquipmentGrade equipmentGrade,
                         String equipmentDescription) {
    if (equipmentName == null || equipmentName.isBlank() || equipmentGrade == null) {
      throw new IllegalArgumentException("Equipment name and grade must not be null or blank.");
    }

    this.equipmentName = equipmentName;
    this.equipmentGrade = equipmentGrade;
    this.equipmentDescription = equipmentDescription;
  }

  public void update(Long equipmentProcessId,
                     Long environmentStandardId,
                     String equipmentCode,
                     String equipmentName,
                     EquipmentStatus equipmentStatus,
                     EquipmentGrade equipmentGrade,
                     String equipmentDescription) {
    if (equipmentProcessId == null || environmentStandardId == null || equipmentCode == null || equipmentCode.isBlank()
        || equipmentName == null || equipmentName.isBlank() || equipmentStatus == null || equipmentGrade == null) {
      throw new IllegalArgumentException("Required equipment fields must not be null or blank.");
    }

    this.equipmentProcessId = equipmentProcessId;
    this.environmentStandardId = environmentStandardId;
    this.equipmentCode = equipmentCode;
    this.equipmentName = equipmentName;
    this.equipmentStatus = equipmentStatus;
    this.equipmentGrade = equipmentGrade;
    this.equipmentDescription = equipmentDescription;
  }
}