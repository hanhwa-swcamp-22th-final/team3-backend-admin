package com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SkillTest {

    @Test
    @DisplayName("월간 기여도를 소폭 누적 성장으로 반영한다")
    void applyMonthlyContributionUpdatesScoreWithAlpha() {
        Skill skill = Skill.builder()
            .skillId(1L)
            .employeeId(100L)
            .skillCategory(SkillCategory.PRODUCTIVITY)
            .skillScore(new BigDecimal("40.00"))
            .evaluatedAt(LocalDateTime.of(2026, 4, 1, 0, 0))
            .build();

        skill.applyMonthlyContribution(
            new BigDecimal("70.00"),
            new BigDecimal("0.01"),
            LocalDateTime.of(2026, 4, 30, 23, 59)
        );

        assertEquals(new BigDecimal("40.70"), skill.getSkillScore());
        assertEquals(LocalDateTime.of(2026, 4, 30, 23, 59), skill.getEvaluatedAt());
    }
}