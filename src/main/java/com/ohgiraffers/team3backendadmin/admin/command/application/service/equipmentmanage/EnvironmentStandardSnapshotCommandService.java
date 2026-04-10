package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.EnvironmentStandardSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher.EnvironmentReferenceEventPublisher;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class EnvironmentStandardSnapshotCommandService {

    private final EnvironmentReferenceEventPublisher environmentReferenceEventPublisher;

    public void publishSnapshotAfterCommit(EnvironmentStandard environmentStandard) {
        EnvironmentStandardSnapshotEvent event = toEvent(environmentStandard, false);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    environmentReferenceEventPublisher.publishEnvironmentStandardSnapshot(event);
                }
            });
            return;
        }
        environmentReferenceEventPublisher.publishEnvironmentStandardSnapshot(event);
    }

    public void publishDeletedSnapshotAfterCommit(EnvironmentStandard environmentStandard) {
        EnvironmentStandardSnapshotEvent event = toEvent(environmentStandard, true);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    environmentReferenceEventPublisher.publishEnvironmentStandardSnapshot(event);
                }
            });
            return;
        }
        environmentReferenceEventPublisher.publishEnvironmentStandardSnapshot(event);
    }

    private EnvironmentStandardSnapshotEvent toEvent(EnvironmentStandard environmentStandard, boolean deleted) {
        return EnvironmentStandardSnapshotEvent.builder()
            .environmentStandardId(environmentStandard.getEnvironmentStandardId())
            .environmentType(environmentStandard.getEnvironmentType() == null ? null : environmentStandard.getEnvironmentType().name())
            .environmentCode(environmentStandard.getEnvironmentCode())
            .environmentName(environmentStandard.getEnvironmentName())
            .envTempMin(environmentStandard.getEnvTempMin())
            .envTempMax(environmentStandard.getEnvTempMax())
            .envHumidityMin(environmentStandard.getEnvHumidityMin())
            .envHumidityMax(environmentStandard.getEnvHumidityMax())
            .envParticleLimit(environmentStandard.getEnvParticleLimit())
            .deleted(deleted || Boolean.TRUE.equals(environmentStandard.getIsDeleted()))
            .occurredAt(LocalDateTime.now())
            .build();
    }
}
