package com.ohgiraffers.team3backendadmin.admin.command.application.service.workerdeployment;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.workerdeployment.WorkerDeployment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.WorkerDeploymentRepository;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.WorkerDeploymentSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher.DeploymentReferenceEventPublisher;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class WorkerDeploymentSnapshotCommandService {

    private final WorkerDeploymentRepository workerDeploymentRepository;
    private final DeploymentReferenceEventPublisher deploymentReferenceEventPublisher;

    public void publishSnapshotAfterCommit(Long workerDeploymentId) {
        WorkerDeployment deployment = workerDeploymentRepository.findById(workerDeploymentId)
            .orElseThrow(() -> new IllegalArgumentException("Worker deployment was not found."));
        publishSnapshotAfterCommit(deployment);
    }

    public void publishSnapshotAfterCommit(WorkerDeployment deployment) {
        WorkerDeploymentSnapshotEvent event = toEvent(deployment);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    deploymentReferenceEventPublisher.publishWorkerDeploymentSnapshot(event);
                }
            });
            return;
        }

        deploymentReferenceEventPublisher.publishWorkerDeploymentSnapshot(event);
    }

    private WorkerDeploymentSnapshotEvent toEvent(WorkerDeployment deployment) {
        return WorkerDeploymentSnapshotEvent.builder()
            .workerDeploymentId(deployment.getWorkerDeploymentId())
            .employeeId(deployment.getEmployeeId())
            .equipmentId(deployment.getEquipmentId())
            .workerDeploymentRole(
                deployment.getWorkerDeploymentRole() == null ? null : deployment.getWorkerDeploymentRole().name()
            )
            .startDate(deployment.getStartDate())
            .endDate(deployment.getEndDate())
            .shift(deployment.getShift() == null ? null : deployment.getShift().name())
            .occurredAt(LocalDateTime.now())
            .build();
    }
}
