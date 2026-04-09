package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceLog;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.MaintenanceLogRepository;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.MaintenanceLogSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher.MaintenanceReferenceEventPublisher;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class MaintenanceLogSnapshotCommandService {

    private final MaintenanceLogRepository maintenanceLogRepository;
    private final MaintenanceReferenceEventPublisher maintenanceReferenceEventPublisher;

    public void publishSnapshotAfterCommit(Long maintenanceLogId) {
        maintenanceLogRepository.findById(maintenanceLogId).ifPresent(this::publishSnapshotAfterCommit);
    }

    public void publishSnapshotAfterCommit(MaintenanceLog maintenanceLog) {
        MaintenanceLogSnapshotEvent event = toEvent(maintenanceLog, false);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    maintenanceReferenceEventPublisher.publishMaintenanceLogSnapshot(event);
                }
            });
            return;
        }
        maintenanceReferenceEventPublisher.publishMaintenanceLogSnapshot(event);
    }

    public void publishDeletedSnapshotAfterCommit(MaintenanceLog maintenanceLog) {
        MaintenanceLogSnapshotEvent event = toEvent(maintenanceLog, true);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    maintenanceReferenceEventPublisher.publishMaintenanceLogSnapshot(event);
                }
            });
            return;
        }
        maintenanceReferenceEventPublisher.publishMaintenanceLogSnapshot(event);
    }

    private MaintenanceLogSnapshotEvent toEvent(MaintenanceLog maintenanceLog, boolean deleted) {
        return MaintenanceLogSnapshotEvent.builder()
            .maintenanceLogId(maintenanceLog.getMaintenanceLogId())
            .equipmentId(maintenanceLog.getEquipmentId())
            .maintenanceItemStandardId(maintenanceLog.getMaintenanceItemStandardId())
            .maintenanceType(maintenanceLog.getMaintenanceType() == null ? null : maintenanceLog.getMaintenanceType().name())
            .maintenanceDate(maintenanceLog.getMaintenanceDate())
            .maintenanceScore(maintenanceLog.getMaintenanceScore())
            .etaMaintDelta(maintenanceLog.getEtaMaintDelta())
            .maintenanceResult(maintenanceLog.getMaintenanceResult() == null ? null : maintenanceLog.getMaintenanceResult().name())
            .deleted(deleted)
            .occurredAt(LocalDateTime.now())
            .build();
    }
}
