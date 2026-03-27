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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipment_baseline")
@EntityListeners(AuditingEntityListener.class)
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