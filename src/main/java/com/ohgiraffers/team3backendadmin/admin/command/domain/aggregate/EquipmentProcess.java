package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Table(name = "equipment_process")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class EquipmentProcess {

    @Id
    @Column(name = "equipment_process_id")
    private Long equipmentProcessId;

    @Column(name = "factory_line_id", nullable = false)
    private Long factoryLineId;

    @Column(name = "equipment_process_code", nullable = false, unique = true)
    private String equipmentProcessCode;

    @Column(name = "equipment_process_name", nullable = false)
    private String equipmentProcessName;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

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

    public EquipmentProcess(Long equipmentProcessId,
                            Long factoryLineId,
                            String equipmentProcessCode,
                            String equipmentProcessName) {
        this(equipmentProcessId, factoryLineId, equipmentProcessCode, equipmentProcessName, false, null, null, null, null);
    }

    @Builder
    public EquipmentProcess(Long equipmentProcessId,
                            Long factoryLineId,
                            String equipmentProcessCode,
                            String equipmentProcessName,
                            Boolean isDeleted,
                            LocalDateTime createdAt,
                            Long createdBy,
                            LocalDateTime updatedAt,
                            Long updatedBy) {
        this.equipmentProcessId = equipmentProcessId;
        this.factoryLineId = factoryLineId;
        this.equipmentProcessCode = equipmentProcessCode;
        this.equipmentProcessName = equipmentProcessName;
        this.isDeleted = isDeleted == null ? false : isDeleted;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

  public void updateInfo(Long factoryLineId,
                         String equipmentProcessCode,
                         String equipmentProcessName) {
    if (factoryLineId == null
        || equipmentProcessCode == null || equipmentProcessCode.isBlank()
        || equipmentProcessName == null || equipmentProcessName.isBlank()) {
      throw new IllegalArgumentException("Factory line id, equipment process code, and name must not be null or blank.");
    }

    this.factoryLineId = factoryLineId;
    this.equipmentProcessCode = equipmentProcessCode;
    this.equipmentProcessName = equipmentProcessName;
  }

  public void softDelete() {
    this.isDeleted = true;
  }
}
