package com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher;

import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.MaintenanceItemStandardSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.MaintenanceLogSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.support.MaintenanceReferenceKafkaTopics;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceReferenceEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceReferenceEventPublisher.class);

    private final KafkaTemplate<String, MaintenanceLogSnapshotEvent> maintenanceLogSnapshotKafkaTemplate;
    private final KafkaTemplate<String, MaintenanceItemStandardSnapshotEvent> maintenanceItemStandardSnapshotKafkaTemplate;

    public void publishMaintenanceLogSnapshot(MaintenanceLogSnapshotEvent event) {
        maintenanceLogSnapshotKafkaTemplate.send(
            MaintenanceReferenceKafkaTopics.MAINTENANCE_LOG_SNAPSHOT,
            String.valueOf(event.getMaintenanceLogId()),
            event
        );
        log.info("Published maintenance log snapshot. maintenanceLogId={}, deleted={}", event.getMaintenanceLogId(), event.getDeleted());
    }

    public void publishMaintenanceItemStandardSnapshot(MaintenanceItemStandardSnapshotEvent event) {
        maintenanceItemStandardSnapshotKafkaTemplate.send(
            MaintenanceReferenceKafkaTopics.MAINTENANCE_ITEM_STANDARD_SNAPSHOT,
            String.valueOf(event.getMaintenanceItemStandardId()),
            event
        );
        log.info(
            "Published maintenance item standard snapshot. maintenanceItemStandardId={}, deleted={}",
            event.getMaintenanceItemStandardId(),
            event.getDeleted()
        );
    }
}
