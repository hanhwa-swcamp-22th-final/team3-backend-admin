package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "skill")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @Column(name = "skill_id")
    private Long skillId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_category")
    private SkillCategory skillCategory;

    @Column(name = "skill_score", precision = 10, scale = 2)
    private BigDecimal skillScore;

    @Column(name = "evaluated_at")
    private LocalDateTime evaluatedAt;

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

    public void updateScore(BigDecimal score) {
        this.skillScore = score;
        this.evaluatedAt = LocalDateTime.now();
    }

    public void applyMonthlyContribution(BigDecimal contributionScore, BigDecimal alpha, LocalDateTime evaluatedAt) {
        BigDecimal current = skillScore == null ? BigDecimal.ZERO : skillScore;
        BigDecimal safeAlpha = alpha == null ? new BigDecimal("0.01") : alpha;
        BigDecimal safeContribution = contributionScore == null ? BigDecimal.ZERO : contributionScore;

        BigDecimal nextScore = current
            .add(safeContribution.multiply(safeAlpha))
            .max(BigDecimal.ZERO)
            .min(new BigDecimal("100.00"))
            .setScale(2, RoundingMode.HALF_UP);

        this.skillScore = nextScore;
        this.evaluatedAt = evaluatedAt == null ? LocalDateTime.now() : evaluatedAt;
    }
}
