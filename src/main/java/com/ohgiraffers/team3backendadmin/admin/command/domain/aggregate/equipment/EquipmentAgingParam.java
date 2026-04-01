package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment;

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
@Table(name = "equipment_aging_param")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class EquipmentAgingParam {

    @Id
    @Column(name = "equipment_aging_param_id")
    private Long equipmentAgingParamId;

    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;

    @Column(name = "equipment_eta_age")
    private BigDecimal equipmentEtaAge;

    @Column(name = "equipment_warranty_month")
    private Integer equipmentWarrantyMonth;

    @Column(name = "equipment_design_life_months")
    private Integer equipmentDesignLifeMonths;

    @Column(name = "equipment_wear_coefficient")
    private BigDecimal equipmentWearCoefficient;

    @Column(name = "equipment_age_months")
    private Integer equipmentAgeMonths;

    @Column(name = "equipment_age_calculated_at")
    private LocalDateTime equipmentAgeCalculatedAt;

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
    public EquipmentAgingParam(Long equipmentAgingParamId,
                               Long equipmentId,
                               BigDecimal equipmentEtaAge,
                               Integer equipmentWarrantyMonth,
                               Integer equipmentDesignLifeMonths,
                               BigDecimal equipmentWearCoefficient,
                               Integer equipmentAgeMonths,
                               LocalDateTime equipmentAgeCalculatedAt,
                               LocalDateTime createdAt,
                               Long createdBy,
                               LocalDateTime updatedAt,
                               Long updatedBy) {
        this.equipmentAgingParamId = equipmentAgingParamId;
        this.equipmentId = equipmentId;
        this.equipmentEtaAge = equipmentEtaAge;
        this.equipmentWarrantyMonth = equipmentWarrantyMonth;
        this.equipmentDesignLifeMonths = equipmentDesignLifeMonths;
        this.equipmentWearCoefficient = equipmentWearCoefficient;
        this.equipmentAgeMonths = equipmentAgeMonths;
        this.equipmentAgeCalculatedAt = equipmentAgeCalculatedAt;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public void update(Integer equipmentWarrantyMonth,
                       Integer equipmentDesignLifeMonths,
                       BigDecimal equipmentWearCoefficient) {
        validateSpecification(equipmentWarrantyMonth, equipmentDesignLifeMonths, equipmentWearCoefficient);

        this.equipmentWarrantyMonth = equipmentWarrantyMonth;
        this.equipmentDesignLifeMonths = equipmentDesignLifeMonths;
        this.equipmentWearCoefficient = equipmentWearCoefficient;
    }

    public void updateCalculationInfo(BigDecimal equipmentEtaAge,
                                      Integer equipmentAgeMonths,
                                      LocalDateTime equipmentAgeCalculatedAt) {
        validateCalculationInfo(equipmentEtaAge, equipmentAgeMonths, equipmentAgeCalculatedAt);

        this.equipmentEtaAge = equipmentEtaAge;
        this.equipmentAgeMonths = equipmentAgeMonths;
        this.equipmentAgeCalculatedAt = equipmentAgeCalculatedAt;
    }

    private void validateSpecification(Integer equipmentWarrantyMonth,
                                       Integer equipmentDesignLifeMonths,
                                       BigDecimal equipmentWearCoefficient) {
        if (equipmentWarrantyMonth == null) {
            throw new IllegalArgumentException("Equipment warranty month must not be null.");
        }
        if (equipmentWarrantyMonth < 0) {
            throw new IllegalArgumentException("Equipment warranty month must not be negative.");
        }
        if (equipmentDesignLifeMonths == null) {
            throw new IllegalArgumentException("Equipment design life months must not be null.");
        }
        if (equipmentDesignLifeMonths < 0) {
            throw new IllegalArgumentException("Equipment design life months must not be negative.");
        }
        if (equipmentWearCoefficient == null) {
            throw new IllegalArgumentException("Equipment wear coefficient must not be null.");
        }
        if (equipmentWearCoefficient.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Equipment wear coefficient must not be negative.");
        }
    }

    private void validateCalculationInfo(BigDecimal equipmentEtaAge,
                                         Integer equipmentAgeMonths,
                                         LocalDateTime equipmentAgeCalculatedAt) {
        if (equipmentEtaAge != null && equipmentEtaAge.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Equipment eta age must not be negative.");
        }
        if (equipmentAgeMonths != null && equipmentAgeMonths < 0) {
            throw new IllegalArgumentException("Equipment age months must not be negative.");
        }
        if (equipmentEtaAge != null && equipmentAgeCalculatedAt == null) {
            throw new IllegalArgumentException("Equipment age calculated at must not be null when eta age exists.");
        }
    }
}
