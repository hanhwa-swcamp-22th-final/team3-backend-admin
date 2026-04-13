package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentAgingParam;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentBaseline;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentAgingParamRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentBaselineRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.EquipmentBaselineCalculatedEvent;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentBaselineCalculatedCommandService {

    private static final Logger log = LoggerFactory.getLogger(EquipmentBaselineCalculatedCommandService.class);

    private final EquipmentRepository equipmentRepository;
    private final EquipmentAgingParamRepository equipmentAgingParamRepository;
    private final EquipmentBaselineRepository equipmentBaselineRepository;
    private final EquipmentReferenceSnapshotCommandService equipmentReferenceSnapshotCommandService;
    private final IdGenerator idGenerator;

    public void applyCalculatedBaseline(EquipmentBaselineCalculatedEvent event) {
        if (event == null || event.getEquipmentId() == null) {
            log.warn("Skipping equipment baseline event without equipmentId.");
            return;
        }

        if (!equipmentRepository.existsById(event.getEquipmentId())) {
            log.warn("Skipping equipment baseline event for missing equipment. equipmentId={}", event.getEquipmentId());
            return;
        }

        EquipmentAgingParam agingParam = equipmentAgingParamRepository
            .findFirstByEquipmentIdOrderByEquipmentAgeCalculatedAtDescEquipmentAgingParamIdDesc(event.getEquipmentId())
            .orElse(null);

        if (agingParam == null) {
            log.warn("Skipping equipment baseline event without aging param. equipmentId={}", event.getEquipmentId());
            return;
        }

        LocalDateTime calculatedAt = event.getCalculatedAt() == null ? LocalDateTime.now() : event.getCalculatedAt();
        agingParam.updateCalculationInfo(
            event.getEquipmentEtaAge(),
            event.getEquipmentAgeMonths(),
            calculatedAt
        );

        EquipmentBaseline baseline = EquipmentBaseline.builder()
            .equipmentBaselineId(idGenerator.generate())
            .equipmentId(event.getEquipmentId())
            .equipmentAgingParamId(agingParam.getEquipmentAgingParamId())
            .equipmentStandardPerformanceRate(event.getEquipmentStandardPerformanceRate())
            .equipmentBaselineErrorRate(event.getEquipmentBaselineErrorRate())
            .equipmentEtaMaint(event.getEquipmentEtaMaint())
            .equipmentIdx(event.getEquipmentIdx())
            .currentEquipmentGrade(resolveEquipmentGrade(event.getCurrentEquipmentGrade()))
            .equipmentBaselineCalculatedAt(calculatedAt)
            .build();

        equipmentBaselineRepository.save(baseline);
        equipmentReferenceSnapshotCommandService.publishSnapshotAfterCommit(event.getEquipmentId());
    }

    private EquipmentGrade resolveEquipmentGrade(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return EquipmentGrade.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            log.warn("Ignoring unknown current equipment grade. grade={}", value);
            return null;
        }
    }
}
