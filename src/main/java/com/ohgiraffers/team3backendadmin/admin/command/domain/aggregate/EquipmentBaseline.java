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
@Table(name = "equipment_baseline")
@Getter
@NoArgsConstructor
public class EquipmentBaseline {

    @Id
    @Column(name = "equipment_baseline_id")
    private Long equipmentBaselineId;

    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;

    @Column(name = "equipment_aging_param_id", nullable = false)
    private Long equipmentAgingParamId;

    @Column(name = "equipment_standard_performance_rate")
    private BigDecimal equipmentStandardPerformanceRate;

    @Column(name = "equipment_baseline_error_rate")
    private BigDecimal equipmentBaselineErrorRate;

    @Column(name = "equipment_eta_maint")
    private BigDecimal equipmentEtaMaint;

    @Column(name = "equipment_idx")
    private BigDecimal equipmentIdx;

    @Column(name = "equipment_baseline_calculated_at")
    private LocalDateTime equipmentBaselineCalculatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Builder
    public EquipmentBaseline(Long equipmentBaselineId,
                             Long equipmentId,
                             Long equipmentAgingParamId,
                             BigDecimal equipmentStandardPerformanceRate,
                             BigDecimal equipmentBaselineErrorRate,
                             BigDecimal equipmentEtaMaint,
                             BigDecimal equipmentIdx,
                             LocalDateTime equipmentBaselineCalculatedAt,
                             LocalDateTime createdAt,
                             Long createdBy,
                             LocalDateTime updatedAt,
                             Long updatedBy) {
        this.equipmentBaselineId = equipmentBaselineId;
        this.equipmentId = equipmentId;
        this.equipmentAgingParamId = equipmentAgingParamId;
        this.equipmentStandardPerformanceRate = equipmentStandardPerformanceRate;
        this.equipmentBaselineErrorRate = equipmentBaselineErrorRate;
        this.equipmentEtaMaint = equipmentEtaMaint;
        this.equipmentIdx = equipmentIdx;
        this.equipmentBaselineCalculatedAt = equipmentBaselineCalculatedAt;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}