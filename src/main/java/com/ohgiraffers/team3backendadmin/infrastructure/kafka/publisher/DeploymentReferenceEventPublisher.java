package com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher;

import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.WorkerDeploymentSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.support.DeploymentReferenceKafkaTopics;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeploymentReferenceEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DeploymentReferenceEventPublisher.class);

    private final KafkaTemplate<String, WorkerDeploymentSnapshotEvent> workerDeploymentSnapshotKafkaTemplate;

    public void publishWorkerDeploymentSnapshot(WorkerDeploymentSnapshotEvent event) {
        workerDeploymentSnapshotKafkaTemplate.send(
            DeploymentReferenceKafkaTopics.WORKER_DEPLOYMENT_SNAPSHOT,
            String.valueOf(event.getWorkerDeploymentId()),
            event
        );
        log.info(
            "Published worker deployment snapshot. deploymentId={}, employeeId={}, equipmentId={}, role={}",
            event.getWorkerDeploymentId(),
            event.getEmployeeId(),
            event.getEquipmentId(),
            event.getWorkerDeploymentRole()
        );
    }
}
