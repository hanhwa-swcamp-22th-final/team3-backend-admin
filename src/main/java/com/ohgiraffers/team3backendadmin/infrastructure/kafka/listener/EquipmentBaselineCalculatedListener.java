package com.ohgiraffers.team3backendadmin.infrastructure.kafka.listener;

import com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage.EquipmentBaselineCalculatedCommandService;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.EquipmentBaselineCalculatedEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.support.EquipmentBaselineKafkaTopics;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EquipmentBaselineCalculatedListener {

    private static final Logger log = LoggerFactory.getLogger(EquipmentBaselineCalculatedListener.class);

    private final EquipmentBaselineCalculatedCommandService equipmentBaselineCalculatedCommandService;

    @KafkaListener(
        topics = EquipmentBaselineKafkaTopics.EQUIPMENT_BASELINE_CALCULATED,
        containerFactory = "equipmentBaselineCalculatedKafkaListenerContainerFactory"
    )
    public void listen(EquipmentBaselineCalculatedEvent event) {
        log.info(
            "Received equipment baseline calculated event. equipmentId={}, equipmentIdx={}, currentEquipmentGrade={}",
            event.getEquipmentId(),
            event.getEquipmentIdx(),
            event.getCurrentEquipmentGrade()
        );
        equipmentBaselineCalculatedCommandService.applyCalculatedBaseline(event);
    }
}
