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
@Table(name = "equipment_aging_param")
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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
        this.equipmentWarrantyMonth = equipmentWarrantyMonth;
        this.equipmentDesignLifeMonths = equipmentDesignLifeMonths;
        this.equipmentWearCoefficient = equipmentWearCoefficient;
    }
}
