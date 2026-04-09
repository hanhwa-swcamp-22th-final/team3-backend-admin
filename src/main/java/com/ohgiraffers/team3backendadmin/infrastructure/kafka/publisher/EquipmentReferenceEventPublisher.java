package com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher;

import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.EquipmentReferenceSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.support.EquipmentReferenceKafkaTopics;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EquipmentReferenceEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EquipmentReferenceEventPublisher.class);

    private final KafkaTemplate<String, EquipmentReferenceSnapshotEvent> equipmentReferenceSnapshotKafkaTemplate;

    public void publishSnapshot(EquipmentReferenceSnapshotEvent event) {
        equipmentReferenceSnapshotKafkaTemplate.send(
            EquipmentReferenceKafkaTopics.EQUIPMENT_REFERENCE_SNAPSHOT,
            String.valueOf(event.getEquipmentId()),
            event
        );
        log.info(
            "Published equipment reference snapshot event. equipmentId={}, equipmentCode={}, deleted={}",
            event.getEquipmentId(),
            event.getEquipmentCode(),
            event.getDeleted()
        );
    }
}
