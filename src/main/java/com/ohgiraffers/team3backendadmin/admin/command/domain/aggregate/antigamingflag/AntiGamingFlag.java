package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.antigamingflag;

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
@Table(name = "anti_gaming_flag")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AntiGamingFlag {

    @Id
    @Column(name = "flag_id")
    private Long flagId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "production_speed_rank")
    private Integer productionSpeedRank;

    @Column(name = "safety_keyword_rank")
    private Integer safetyKeywordRank;

    @Column(name = "penalty_coefficient")
    private BigDecimal penaltyCoefficient;

    @Column(name = "target_year")
    private Integer targetYear;

    @Column(name = "target_period", length = 50)
    private String targetPeriod;

    @Column(name = "is_active")
    private Boolean isActive;

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
}
