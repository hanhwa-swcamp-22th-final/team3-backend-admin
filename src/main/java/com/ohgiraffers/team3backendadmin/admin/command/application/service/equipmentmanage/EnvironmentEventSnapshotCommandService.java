package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.EnvironmentEventSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher.EnvironmentReferenceEventPublisher;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class EnvironmentEventSnapshotCommandService {

    private final EnvironmentReferenceEventPublisher environmentReferenceEventPublisher;

    public void publishSnapshotAfterCommit(EnvironmentEvent environmentEvent) {
        EnvironmentEventSnapshotEvent event = toEvent(environmentEvent, false);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    environmentReferenceEventPublisher.publishEnvironmentEventSnapshot(event);
                }
            });
            return;
        }
        environmentReferenceEventPublisher.publishEnvironmentEventSnapshot(event);
    }

    public void publishDeletedSnapshotAfterCommit(EnvironmentEvent environmentEvent) {
        EnvironmentEventSnapshotEvent event = toEvent(environmentEvent, true);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    environmentReferenceEventPublisher.publishEnvironmentEventSnapshot(event);
                }
            });
            return;
        }
        environmentReferenceEventPublisher.publishEnvironmentEventSnapshot(event);
    }

    private EnvironmentEventSnapshotEvent toEvent(EnvironmentEvent environmentEvent, boolean deleted) {
        return EnvironmentEventSnapshotEvent.builder()
            .environmentEventId(environmentEvent.getEnvironmentEventId())
            .equipmentId(environmentEvent.getEquipmentId())
            .envTemperature(environmentEvent.getEnvTemperature())
            .envHumidity(environmentEvent.getEnvHumidity())
            .envParticleCnt(environmentEvent.getEnvParticleCnt())
            .envDeviationType(environmentEvent.getEnvDeviationType() == null ? null : environmentEvent.getEnvDeviationType().name())
            .envCorrectionApplied(environmentEvent.getEnvCorrectionApplied())
            .envDetectedAt(environmentEvent.getEnvDetectedAt())
            .deleted(deleted)
            .occurredAt(LocalDateTime.now())
            .build();
    }
}
