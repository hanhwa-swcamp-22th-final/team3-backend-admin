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

    /**
     * 유지보수 항목 기준을 생성한다.
     * @param request 생성할 유지보수 항목 기준 정보
     * @return 생성된 유지보수 항목 기준 응답
     */
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

    /**
     * 유지보수 항목 기준 정보를 수정한다.
     * @param maintenanceItemStandardId 수정할 유지보수 항목 기준 ID
     * @param request 수정할 유지보수 항목 기준 정보
     * @return 수정된 유지보수 항목 기준 응답
     */
    public MaintenanceItemStandardUpdateResponse updateMaintenanceItemStandard(Long maintenanceItemStandardId, MaintenanceItemStandardUpdateRequest request) {
        MaintenanceItemStandard maintenanceItemStandard = maintenanceItemStandardRepository.findById(maintenanceItemStandardId)
            .filter(item -> !Boolean.TRUE.equals(item.getIsDeleted()))
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

    /**
     * 유지보수 항목 기준을 삭제한다.
     * @param maintenanceItemStandardId 삭제할 유지보수 항목 기준 ID
     * @return 삭제된 유지보수 항목 기준 응답
     */
    public MaintenanceItemStandardUpdateResponse deleteMaintenanceItemStandard(Long maintenanceItemStandardId) {
        MaintenanceItemStandard maintenanceItemStandard = maintenanceItemStandardRepository.findById(maintenanceItemStandardId)
            .filter(item -> !Boolean.TRUE.equals(item.getIsDeleted()))
            .orElseThrow(() -> new BusinessException(ErrorCode.MAINTENANCE_ITEM_STANDARD_NOT_FOUND));

        maintenanceItemStandard.softDelete();

        return MaintenanceItemStandardUpdateResponse.builder()
            .maintenanceItemStandardId(maintenanceItemStandard.getMaintenanceItemStandardId())
            .maintenanceItem(maintenanceItemStandard.getMaintenanceItem())
            .maintenanceWeight(maintenanceItemStandard.getMaintenanceWeight())
            .maintenanceScoreMax(maintenanceItemStandard.getMaintenanceScoreMax())
            .build();
    }
}
