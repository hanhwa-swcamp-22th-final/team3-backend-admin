package com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher;

import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.EmployeeSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.support.EmployeeReferenceKafkaTopics;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeReferenceEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EmployeeReferenceEventPublisher.class);

    private final KafkaTemplate<String, EmployeeSnapshotEvent> employeeSnapshotKafkaTemplate;

    public void publishEmployeeSnapshot(EmployeeSnapshotEvent event) {
        employeeSnapshotKafkaTemplate.send(
            EmployeeReferenceKafkaTopics.EMPLOYEE_SNAPSHOT,
            String.valueOf(event.getEmployeeId()),
            event
        );
        log.info(
            "Published employee snapshot. employeeId={}, employeeCode={}, employeeTier={}, employeeStatus={}",
            event.getEmployeeId(),
            event.getEmployeeCode(),
            event.getEmployeeTier(),
            event.getEmployeeStatus()
        );
    }
}
