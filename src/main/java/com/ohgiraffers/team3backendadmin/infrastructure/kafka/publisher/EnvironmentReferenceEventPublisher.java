package com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher;

import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.EnvironmentEventSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.support.EnvironmentReferenceKafkaTopics;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnvironmentReferenceEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EnvironmentReferenceEventPublisher.class);

    private final KafkaTemplate<String, EnvironmentEventSnapshotEvent> environmentEventSnapshotKafkaTemplate;

    public void publishEnvironmentEventSnapshot(EnvironmentEventSnapshotEvent event) {
        environmentEventSnapshotKafkaTemplate.send(
            EnvironmentReferenceKafkaTopics.ENVIRONMENT_EVENT_SNAPSHOT,
            String.valueOf(event.getEnvironmentEventId()),
            event
        );
        log.info("Published environment event snapshot. environmentEventId={}, deleted={}", event.getEnvironmentEventId(), event.getDeleted());
    }
}
