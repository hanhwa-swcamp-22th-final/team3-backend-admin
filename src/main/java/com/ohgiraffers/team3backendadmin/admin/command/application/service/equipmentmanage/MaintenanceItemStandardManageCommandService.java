package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceItemStandardCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.MaintenanceItemStandardUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceItemStandardCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.MaintenanceItemStandardUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.maintenance.MaintenanceItemStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.MaintenanceItemStandardRepository;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceItemStandardManageCommandService {

    private final MaintenanceItemStandardRepository maintenanceItemStandardRepository;
    private final IdGenerator idGenerator;

    public MaintenanceItemStandardCreateResponse createMaintenanceItemStandard(MaintenanceItemStandardCreateRequest request) {
        MaintenanceItemStandard maintenanceItemStandard = MaintenanceItemStandard.builder()
            .maintenanceItemStandardId(idGenerator.generate())
            .maintenanceItem(request.getMaintenanceItem())
            .maintenanceWeight(request.getMaintenanceWeight())
            .maintenanceScoreMax(request.getMaintenanceScoreMax())
            .build();

        maintenanceItemStandardRepository.save(maintenanceItemStandard);

        return MaintenanceItemStandardCreateResponse.builder()
            .maintenanceItemStandardId(maintenanceItemStandard.getMaintenanceItemStandardId())
            .maintenanceItem(maintenanceItemStandard.getMaintenanceItem())
            .maintenanceWeight(maintenanceItemStandard.getMaintenanceWeight())
            .maintenanceScoreMax(maintenanceItemStandard.getMaintenanceScoreMax())
            .build();
    }

    public MaintenanceItemStandardUpdateResponse updateMaintenanceItemStandard(Long maintenanceItemStandardId, MaintenanceItemStandardUpdateRequest request) {
        MaintenanceItemStandard maintenanceItemStandard = maintenanceItemStandardRepository.findById(maintenanceItemStandardId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MAINTENANCE_ITEM_STANDARD_NOT_FOUND));

        maintenanceItemStandard.updateInfo(
            request.getMaintenanceItem(),
            request.getMaintenanceWeight(),
            request.getMaintenanceScoreMax()
        );

        return MaintenanceItemStandardUpdateResponse.builder()
            .maintenanceItemStandardId(maintenanceItemStandard.getMaintenanceItemStandardId())
            .maintenanceItem(maintenanceItemStandard.getMaintenanceItem())
            .maintenanceWeight(maintenanceItemStandard.getMaintenanceWeight())
            .maintenanceScoreMax(maintenanceItemStandard.getMaintenanceScoreMax())
            .build();
    }

    public MaintenanceItemStandardUpdateResponse deleteMaintenanceItemStandard(Long maintenanceItemStandardId) {
        MaintenanceItemStandard maintenanceItemStandard = maintenanceItemStandardRepository.findById(maintenanceItemStandardId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MAINTENANCE_ITEM_STANDARD_NOT_FOUND));

        maintenanceItemStandardRepository.delete(maintenanceItemStandard);

        return MaintenanceItemStandardUpdateResponse.builder()
            .maintenanceItemStandardId(maintenanceItemStandard.getMaintenanceItemStandardId())
            .maintenanceItem(maintenanceItemStandard.getMaintenanceItem())
            .maintenanceWeight(maintenanceItemStandard.getMaintenanceWeight())
            .maintenanceScoreMax(maintenanceItemStandard.getMaintenanceScoreMax())
            .build();
    }
}