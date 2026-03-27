package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_item_standard")
@Getter
@NoArgsConstructor
public class MaintenanceItemStandard {

    @Id
    @Column(name = "maintenance_item_standard_id")
    private Long maintenanceItemStandardId;

    @Column(name = "maintenance_item", nullable = false)
    private String maintenanceItem;

    @Column(name = "maintenance_weight", nullable = false)
    private BigDecimal maintenanceWeight;

    @Column(name = "maintenance_score_max", nullable = false)
    private BigDecimal maintenanceScoreMax;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Builder
    public MaintenanceItemStandard(Long maintenanceItemStandardId,
                                   String maintenanceItem,
                                   BigDecimal maintenanceWeight,
                                   BigDecimal maintenanceScoreMax,
                                   LocalDateTime createdAt,
                                   Long createdBy,
                                   LocalDateTime updatedAt,
                                   Long updatedBy) {
        this.maintenanceItemStandardId = maintenanceItemStandardId;
        this.maintenanceItem = maintenanceItem;
        this.maintenanceWeight = maintenanceWeight;
        this.maintenanceScoreMax = maintenanceScoreMax;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}