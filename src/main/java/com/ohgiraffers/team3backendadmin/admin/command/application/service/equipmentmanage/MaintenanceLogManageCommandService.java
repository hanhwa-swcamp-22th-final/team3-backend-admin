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

    /**
     * 유지보수 이력을 생성한다.
     * @param request 생성할 유지보수 이력 정보
     * @return 생성된 유지보수 이력 응답
     */
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

    /**
     * 유지보수 이력 정보를 수정한다.
     * @param maintenanceLogId 수정할 유지보수 이력 ID
     * @param request 수정할 유지보수 이력 정보
     * @return 수정된 유지보수 이력 응답
     */
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

    /**
     * 유지보수 이력을 삭제한다.
     * @param maintenanceLogId 삭제할 유지보수 이력 ID
     * @return 삭제된 유지보수 이력 응답
     */
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
