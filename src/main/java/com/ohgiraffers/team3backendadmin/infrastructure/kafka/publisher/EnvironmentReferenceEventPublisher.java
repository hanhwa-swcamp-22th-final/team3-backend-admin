package com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher;

import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.EnvironmentStandardSnapshotEvent;
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

    private final KafkaTemplate<String, EnvironmentStandardSnapshotEvent> environmentStandardSnapshotKafkaTemplate;

    public void publishEnvironmentStandardSnapshot(EnvironmentStandardSnapshotEvent event) {
        environmentStandardSnapshotKafkaTemplate.send(
            EnvironmentReferenceKafkaTopics.ENVIRONMENT_STANDARD_SNAPSHOT,
            String.valueOf(event.getEnvironmentStandardId()),
            event
        );
        log.info(
            "Published environment standard snapshot. environmentStandardId={}, deleted={}",
            event.getEnvironmentStandardId(),
            event.getDeleted()
        );
    }
}
