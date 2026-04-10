package com.ohgiraffers.team3backendadmin.infrastructure.kafka.listener;

import com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement.SkillGrowthCommandService;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.SkillGrowthCalculatedEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.support.SkillGrowthKafkaTopics;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SkillGrowthCalculatedListener {

    private static final Logger log = LoggerFactory.getLogger(SkillGrowthCalculatedListener.class);

    private final SkillGrowthCommandService skillGrowthCommandService;

    @KafkaListener(
        topics = SkillGrowthKafkaTopics.SKILL_GROWTH_CALCULATED,
        containerFactory = "skillGrowthCalculatedKafkaListenerContainerFactory"
    )
    public void listen(SkillGrowthCalculatedEvent event) {
        log.info(
            "Received skill growth event. employeeId={}, skillCategory={}, contributionScore={}, alpha={}",
            event.getEmployeeId(),
            event.getSkillCategory(),
            event.getSkillContributionScore(),
            event.getAlpha()
        );
        skillGrowthCommandService.applyCalculatedGrowth(event);
    }
}
