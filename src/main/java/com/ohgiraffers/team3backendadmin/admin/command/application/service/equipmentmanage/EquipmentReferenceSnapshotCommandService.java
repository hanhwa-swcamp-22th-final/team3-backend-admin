package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentAgingParam;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentBaseline;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentStandardRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentAgingParamRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentBaselineRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.EquipmentReferenceSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher.EquipmentReferenceEventPublisher;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class EquipmentReferenceSnapshotCommandService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentAgingParamRepository equipmentAgingParamRepository;
    private final EquipmentBaselineRepository equipmentBaselineRepository;
    private final EnvironmentStandardRepository environmentStandardRepository;
    private final EquipmentReferenceEventPublisher equipmentReferenceEventPublisher;

    public void publishSnapshotAfterCommit(Long equipmentId) {
        publishSnapshotsAfterCommit(Set.of(equipmentId));
    }

    public void publishSnapshotsAfterCommit(Collection<Long> equipmentIds) {
        Set<Long> distinctIds = new LinkedHashSet<>(equipmentIds);
        if (distinctIds.isEmpty()) {
            return;
        }

        Runnable publishAction = () -> distinctIds.forEach(this::publishSnapshot);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishAction.run();
                }
            });
            return;
        }

        publishAction.run();
    }

    public void publishDeletedSnapshotAfterCommit(Long equipmentId, String equipmentCode) {
        Runnable publishAction = () -> equipmentReferenceEventPublisher.publishSnapshot(
            new EquipmentReferenceSnapshotEvent(
                equipmentId,
                equipmentCode,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                true,
                LocalDateTime.now()
            )
        );

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishAction.run();
                }
            });
            return;
        }

        publishAction.run();
    }

    private void publishSnapshot(Long equipmentId) {
        equipmentRepository.findById(equipmentId).ifPresent(equipment -> {
            EquipmentAgingParam agingParam = equipmentAgingParamRepository
                .findFirstByEquipmentIdOrderByEquipmentAgeCalculatedAtDescEquipmentAgingParamIdDesc(equipmentId)
                .orElse(null);
            EquipmentBaseline baseline = equipmentBaselineRepository
                .findFirstByEquipmentIdOrderByEquipmentBaselineCalculatedAtDescEquipmentBaselineIdDesc(equipmentId)
                .orElse(null);
            EnvironmentStandard environmentStandard = environmentStandardRepository.findById(
                equipment.getEnvironmentStandardId()
            ).orElse(null);

            equipmentReferenceEventPublisher.publishSnapshot(
                new EquipmentReferenceSnapshotEvent(
                    equipment.getEquipmentId(),
                    equipment.getEquipmentCode(),
                    equipment.getEquipmentStatus() == null ? null : equipment.getEquipmentStatus().name(),
                    equipment.getEquipmentGrade() == null ? null : equipment.getEquipmentGrade().name(),
                    equipment.getEquipmentInstallDate(),
                    equipment.getEnvironmentStandardId(),
                    agingParam == null ? null : agingParam.getEquipmentWarrantyMonth(),
                    agingParam == null ? null : agingParam.getEquipmentDesignLifeMonths(),
                    agingParam == null ? null : agingParam.getEquipmentWearCoefficient(),
                    baseline == null ? null : baseline.getEquipmentStandardPerformanceRate(),
                    baseline == null ? null : baseline.getEquipmentBaselineErrorRate(),
                    baseline == null ? null : baseline.getEquipmentEtaMaint(),
                    baseline == null ? null : baseline.getEquipmentIdx(),
                    agingParam == null ? null : agingParam.getEquipmentEtaAge(),
                    agingParam == null ? null : agingParam.getEquipmentAgeMonths(),
                    baseline == null || baseline.getCurrentEquipmentGrade() == null
                        ? null
                        : baseline.getCurrentEquipmentGrade().name(),
                    environmentStandard == null ? null : environmentStandard.getEnvTempMin(),
                    environmentStandard == null ? null : environmentStandard.getEnvTempMax(),
                    environmentStandard == null ? null : environmentStandard.getEnvHumidityMin(),
                    environmentStandard == null ? null : environmentStandard.getEnvHumidityMax(),
                    environmentStandard == null ? null : environmentStandard.getEnvParticleLimit(),
                    false,
                    LocalDateTime.now()
                )
            );
        });
    }
}
