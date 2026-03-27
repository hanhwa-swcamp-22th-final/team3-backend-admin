package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipment_process")
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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    public EquipmentProcess(Long equipmentProcessId,
                            Long factoryLineId,
                            String equipmentProcessCode,
                            String equipmentProcessName) {
        this(equipmentProcessId, factoryLineId, equipmentProcessCode, equipmentProcessName, null, null, null, null);
    }

    @Builder
    public EquipmentProcess(Long equipmentProcessId,
                            Long factoryLineId,
                            String equipmentProcessCode,
                            String equipmentProcessName,
                            LocalDateTime createdAt,
                            Long createdBy,
                            LocalDateTime updatedAt,
                            Long updatedBy) {
        this.equipmentProcessId = equipmentProcessId;
        this.factoryLineId = factoryLineId;
        this.equipmentProcessCode = equipmentProcessCode;
        this.equipmentProcessName = equipmentProcessName;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}