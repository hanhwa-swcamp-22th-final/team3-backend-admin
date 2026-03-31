package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance;

import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
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
@Table(name = "maintenance_item_standard")
@EntityListeners(AuditingEntityListener.class)
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

    public void updateInfo(String maintenanceItem,
                           BigDecimal maintenanceWeight,
                           BigDecimal maintenanceScoreMax) {
        validate(maintenanceItem, maintenanceWeight, maintenanceScoreMax);

        this.maintenanceItem = maintenanceItem;
        this.maintenanceWeight = maintenanceWeight;
        this.maintenanceScoreMax = maintenanceScoreMax;
    }

    private void validate(String maintenanceItem,
                          BigDecimal maintenanceWeight,
                          BigDecimal maintenanceScoreMax) {
        if (maintenanceItem == null || maintenanceItem.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "유지보수 항목명은 비어 있을 수 없습니다.");
        }
        if (maintenanceWeight == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "유지보수 가중치는 필수입니다.");
        }
        if (maintenanceWeight.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "유지보수 가중치는 음수일 수 없습니다.");
        }
        if (maintenanceScoreMax == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "유지보수 최대 점수는 필수입니다.");
        }
        if (maintenanceScoreMax.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "유지보수 최대 점수는 음수일 수 없습니다.");
        }
    }
}
