package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceItemStandard;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.MaintenanceItemStandardSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher.MaintenanceReferenceEventPublisher;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class MaintenanceItemStandardSnapshotCommandService {

    private final MaintenanceReferenceEventPublisher maintenanceReferenceEventPublisher;

    public void publishSnapshotAfterCommit(MaintenanceItemStandard standard) {
        MaintenanceItemStandardSnapshotEvent event = toEvent(standard);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    maintenanceReferenceEventPublisher.publishMaintenanceItemStandardSnapshot(event);
                }
            });
            return;
        }
        maintenanceReferenceEventPublisher.publishMaintenanceItemStandardSnapshot(event);
    }

    private MaintenanceItemStandardSnapshotEvent toEvent(MaintenanceItemStandard standard) {
        return MaintenanceItemStandardSnapshotEvent.builder()
            .maintenanceItemStandardId(standard.getMaintenanceItemStandardId())
            .maintenanceItem(standard.getMaintenanceItem())
            .maintenanceWeight(standard.getMaintenanceWeight())
            .maintenanceScoreMax(standard.getMaintenanceScoreMax())
            .deleted(Boolean.TRUE.equals(standard.getIsDeleted()))
            .occurredAt(LocalDateTime.now())
            .build();
    }
}
