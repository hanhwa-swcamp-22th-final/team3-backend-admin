package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceLogCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceLogUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceLogCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceLogUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceLog;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.MaintenanceItemStandardRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.MaintenanceLogRepository;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceLogManageCommandService {

    private final MaintenanceLogRepository maintenanceLogRepository;
    private final EquipmentRepository equipmentRepository;
    private final MaintenanceItemStandardRepository maintenanceItemStandardRepository;
    private final IdGenerator idGenerator;

    public MaintenanceLogCreateResponse createMaintenanceLog(MaintenanceLogCreateRequest request) {
        equipmentRepository.findById(request.getEquipmentId())
            .orElseThrow(() -> new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND));
        maintenanceItemStandardRepository.findById(request.getMaintenanceItemStandardId())
            .orElseThrow(() -> new BusinessException(ErrorCode.MAINTENANCE_ITEM_STANDARD_NOT_FOUND));

        MaintenanceLog maintenanceLog = MaintenanceLog.builder()
            .maintenanceLogId(idGenerator.generate())
            .equipmentId(request.getEquipmentId())
            .maintenanceItemStandardId(request.getMaintenanceItemStandardId())
            .maintenanceType(request.getMaintenanceType())
            .maintenanceDate(request.getMaintenanceDate())
            .maintenanceScore(request.getMaintenanceScore())
            .etaMaintDelta(request.getEtaMaintDelta())
            .maintenanceResult(request.getMaintenanceResult())
            .build();

        maintenanceLogRepository.save(maintenanceLog);

        return MaintenanceLogCreateResponse.builder()
            .maintenanceLogId(maintenanceLog.getMaintenanceLogId())
            .equipmentId(maintenanceLog.getEquipmentId())
            .maintenanceItemStandardId(maintenanceLog.getMaintenanceItemStandardId())
            .maintenanceType(maintenanceLog.getMaintenanceType())
            .maintenanceResult(maintenanceLog.getMaintenanceResult())
            .build();
    }

    public MaintenanceLogUpdateResponse updateMaintenanceLog(Long maintenanceLogId, MaintenanceLogUpdateRequest request) {
        MaintenanceLog maintenanceLog = maintenanceLogRepository.findById(maintenanceLogId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MAINTENANCE_LOG_NOT_FOUND));

        equipmentRepository.findById(request.getEquipmentId())
            .orElseThrow(() -> new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND));
        maintenanceItemStandardRepository.findById(request.getMaintenanceItemStandardId())
            .orElseThrow(() -> new BusinessException(ErrorCode.MAINTENANCE_ITEM_STANDARD_NOT_FOUND));

        maintenanceLog.updateInfo(
            request.getEquipmentId(),
            request.getMaintenanceItemStandardId(),
            request.getMaintenanceType(),
            request.getMaintenanceDate(),
            request.getMaintenanceScore(),
            request.getEtaMaintDelta(),
            request.getMaintenanceResult()
        );

        return MaintenanceLogUpdateResponse.builder()
            .maintenanceLogId(maintenanceLog.getMaintenanceLogId())
            .equipmentId(maintenanceLog.getEquipmentId())
            .maintenanceItemStandardId(maintenanceLog.getMaintenanceItemStandardId())
            .maintenanceType(maintenanceLog.getMaintenanceType())
            .maintenanceResult(maintenanceLog.getMaintenanceResult())
            .build();
    }

    public MaintenanceLogUpdateResponse deleteMaintenanceLog(Long maintenanceLogId) {
        MaintenanceLog maintenanceLog = maintenanceLogRepository.findById(maintenanceLogId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MAINTENANCE_LOG_NOT_FOUND));

        maintenanceLogRepository.delete(maintenanceLog);

        return MaintenanceLogUpdateResponse.builder()
            .maintenanceLogId(maintenanceLog.getMaintenanceLogId())
            .equipmentId(maintenanceLog.getEquipmentId())
            .maintenanceItemStandardId(maintenanceLog.getMaintenanceItemStandardId())
            .maintenanceType(maintenanceLog.getMaintenanceType())
            .maintenanceResult(maintenanceLog.getMaintenanceResult())
            .build();
    }
}