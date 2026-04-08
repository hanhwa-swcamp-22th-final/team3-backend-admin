package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance;

import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_log")
@EntityListeners(AuditingEntityListener.class)
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

    public void updateInfo(Long equipmentId,
                           Long maintenanceItemStandardId,
                           MaintenanceType maintenanceType,
                           LocalDate maintenanceDate,
                           BigDecimal maintenanceScore,
                           BigDecimal etaMaintDelta,
                           MaintenanceResult maintenanceResult) {
        validate(equipmentId, maintenanceItemStandardId, maintenanceType, maintenanceDate,
            maintenanceScore, etaMaintDelta, maintenanceResult);

        this.equipmentId = equipmentId;
        this.maintenanceItemStandardId = maintenanceItemStandardId;
        this.maintenanceType = maintenanceType;
        this.maintenanceDate = maintenanceDate;
        this.maintenanceScore = maintenanceScore;
        this.etaMaintDelta = etaMaintDelta;
        this.maintenanceResult = maintenanceResult;
    }

    private void validate(Long equipmentId,
                          Long maintenanceItemStandardId,
                          MaintenanceType maintenanceType,
                          LocalDate maintenanceDate,
                          BigDecimal maintenanceScore,
                          BigDecimal etaMaintDelta,
                          MaintenanceResult maintenanceResult) {
        if (equipmentId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "설비 ID는 필수입니다.");
        }
        if (maintenanceItemStandardId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "유지보수 항목 기준 ID는 필수입니다.");
        }
        if (maintenanceType == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "유지보수 유형은 필수입니다.");
        }
        if (maintenanceDate == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "유지보수 일자는 필수입니다.");
        }
        if (maintenanceScore != null && maintenanceScore.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "유지보수 점수는 음수일 수 없습니다.");
        }
        if (etaMaintDelta != null && etaMaintDelta.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "ETA 유지보수 편차는 음수일 수 없습니다.");
        }
        if (maintenanceResult == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "유지보수 결과는 필수입니다.");
        }
    }
}
