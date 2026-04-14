package com.ohgiraffers.team3backendadmin.infrastructure.kafka.listener;

import com.ohgiraffers.team3backendadmin.admin.command.application.service.score.ScoreSnapshotCommandService;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.PerformancePointSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.support.PromotionKafkaTopics;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PerformancePointSnapshotListener {

    private static final Logger log = LoggerFactory.getLogger(PerformancePointSnapshotListener.class);

    private final ScoreSnapshotCommandService scoreSnapshotCommandService;

    @KafkaListener(
        topics = PromotionKafkaTopics.PERFORMANCE_POINT_SNAPSHOT,
        containerFactory = "performancePointSnapshotKafkaListenerContainerFactory"
    )
    public void listen(PerformancePointSnapshotEvent event) {
        log.info(
            "Received performance point snapshot event. employeeId={}, pointType={}, pointAmount={}, pointSourceId={}",
            event.getEmployeeId(),
            event.getPointType(),
            event.getPointAmount(),
            event.getPointSourceId()
        );
        scoreSnapshotCommandService.refreshAfterPerformancePointSnapshot(event);
    }
}
