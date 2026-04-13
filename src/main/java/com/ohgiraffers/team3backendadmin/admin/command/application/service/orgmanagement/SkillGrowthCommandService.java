package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.Skill;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.SkillRepository;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.score.ScoreSnapshotCommandService;
import com.ohgiraffers.team3backendadmin.common.exception.SkillNotFoundException;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.SkillGrowthCalculatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SkillGrowthCommandService {

    private final SkillRepository skillRepository;
    private final ScoreSnapshotCommandService scoreSnapshotCommandService;

    @Transactional
    public void applyCalculatedGrowth(SkillGrowthCalculatedEvent event) {
        SkillCategory skillCategory = SkillCategory.valueOf(event.getSkillCategory());
        Skill skill = skillRepository.findByEmployeeIdAndSkillCategory(event.getEmployeeId(), skillCategory)
            .orElseThrow(() -> new SkillNotFoundException(
                "해당 스킬 레코드를 찾을 수 없습니다: employeeId=" + event.getEmployeeId()
                    + ", skillCategory=" + event.getSkillCategory()
            ));

        skill.applyMonthlyContribution(
            event.getSkillContributionScore(),
            event.getAlpha(),
            event.getOccurredAt()
        );

        scoreSnapshotCommandService.refreshAfterSkillGrowth(
            event.getEmployeeId(),
            event.getEvaluationPeriodId()
        );
    }
}
