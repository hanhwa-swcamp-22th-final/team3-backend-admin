package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.Skill;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.SkillRepository;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.SkillGrowthCalculatedEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SkillGrowthCommandServiceTest {

    @InjectMocks
    private SkillGrowthCommandService skillGrowthCommandService;

    @Mock
    private SkillRepository skillRepository;

    @Test
    @DisplayName("월간 skill growth 이벤트를 받아 skill score를 소폭 누적 갱신한다")
    void applyCalculatedGrowthUpdatesSkillScore() {
        Skill skill = Skill.builder()
            .skillId(1L)
            .employeeId(5000L)
            .skillCategory(SkillCategory.QUALITY_MANAGEMENT)
            .skillScore(new BigDecimal("50.00"))
            .evaluatedAt(LocalDateTime.of(2026, 4, 1, 0, 0))
            .build();

        SkillGrowthCalculatedEvent event = SkillGrowthCalculatedEvent.builder()
            .employeeId(5000L)
            .skillCategory("QUALITY_MANAGEMENT")
            .skillContributionScore(new BigDecimal("65.00"))
            .alpha(new BigDecimal("0.01"))
            .occurredAt(LocalDateTime.of(2026, 4, 30, 23, 59))
            .build();

        given(skillRepository.findByEmployeeIdAndSkillCategory(5000L, SkillCategory.QUALITY_MANAGEMENT))
            .willReturn(Optional.of(skill));

        skillGrowthCommandService.applyCalculatedGrowth(event);

        assertEquals(new BigDecimal("50.65"), skill.getSkillScore());
        assertEquals(LocalDateTime.of(2026, 4, 30, 23, 59), skill.getEvaluatedAt());
    }
}