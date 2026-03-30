package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_log")
@Getter
@NoArgsConstructor
public class MaintenanceLog {

    @Id
    @Column(name = "maintenance_log_id")
    private Long maintenanceLogId;

    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;

    @Column(name = "maintenance_item_standard_id", nullable = false)
    private Long maintenanceItemStandardId;

    @Enumerated(EnumType.STRING)
    @Column(name = "maintenance_type", nullable = false)
    private MaintenanceType maintenanceType;

    @Column(name = "maintenance_date", nullable = false)
    private LocalDate maintenanceDate;

    @Column(name = "maintenance_score")
    private BigDecimal maintenanceScore;

    @Column(name = "eta_maint_delta")
    private BigDecimal etaMaintDelta;

    @Enumerated(EnumType.STRING)
    @Column(name = "maintenance_result", nullable = false)
    private MaintenanceResult maintenanceResult;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Builder
    public MaintenanceLog(Long maintenanceLogId,
                          Long equipmentId,
                          Long maintenanceItemStandardId,
                          MaintenanceType maintenanceType,
                          LocalDate maintenanceDate,
                          BigDecimal maintenanceScore,
                          BigDecimal etaMaintDelta,
                          MaintenanceResult maintenanceResult,
                          LocalDateTime createdAt,
                          Long createdBy,
                          LocalDateTime updatedAt,
                          Long updatedBy) {
        this.maintenanceLogId = maintenanceLogId;
        this.equipmentId = equipmentId;
        this.maintenanceItemStandardId = maintenanceItemStandardId;
        this.maintenanceType = maintenanceType;
        this.maintenanceDate = maintenanceDate;
        this.maintenanceScore = maintenanceScore;
        this.etaMaintDelta = etaMaintDelta;
        this.maintenanceResult = maintenanceResult;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}