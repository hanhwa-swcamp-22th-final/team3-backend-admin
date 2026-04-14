package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.score;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "score")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Score {

    @Id
    @Column(name = "score_id")
    private Long scoreId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "eval_period_id", nullable = false)
    private Long evalPeriodId;

    @Column(name = "capability_index")
    private BigDecimal capabilityIndex;

    @Column(name = "total_points")
    private Integer totalPoints;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier")
    private EmployeeTier tier;

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

    public static Score create(
        Long scoreId,
        Long employeeId,
        Long evalPeriodId,
        BigDecimal capabilityIndex,
        Integer totalPoints,
        EmployeeTier tier
    ) {
        return Score.builder()
            .scoreId(scoreId)
            .employeeId(employeeId)
            .evalPeriodId(evalPeriodId)
            .capabilityIndex(capabilityIndex)
            .totalPoints(totalPoints)
            .tier(tier)
            .build();
    }

    public void updateSnapshot(BigDecimal capabilityIndex, Integer totalPoints, EmployeeTier tier) {
        if (capabilityIndex != null) {
            this.capabilityIndex = capabilityIndex;
        }
        if (totalPoints != null) {
            this.totalPoints = totalPoints;
        }
        if (tier != null) {
            this.tier = tier;
        }
    }
}
