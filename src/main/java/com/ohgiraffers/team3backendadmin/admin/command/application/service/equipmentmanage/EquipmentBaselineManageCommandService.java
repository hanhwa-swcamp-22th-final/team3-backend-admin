package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentBaselineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentBaselineUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentBaseline;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentBaselineRepository;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentBaselineManageCommandService {

    private final EquipmentBaselineRepository equipmentBaselineRepository;

    /**
     * 설비 baseline 정보를 수정한다.
     * 초기 보증값과 배치 계산값 모두 동일한 baseline 컬럼에 반영한다.
     * @param equipmentBaselineId 수정할 설비 baseline 식별자
     * @param request baseline 성능, 오차율, ETA, index, 계산 시각 정보
     * @return 수정된 설비 baseline 정보
     */
    public EquipmentBaselineUpdateResponse updateEquipmentBaseline(Long equipmentBaselineId,
                                                                   EquipmentBaselineUpdateRequest request) {
        EquipmentBaseline equipmentBaseline = equipmentBaselineRepository.findById(equipmentBaselineId)
            .orElseThrow(() -> new BusinessException(ErrorCode.EQUIPMENT_BASELINE_NOT_FOUND));

        equipmentBaseline.update(
            request.getEquipmentStandardPerformanceRate(),
            request.getEquipmentBaselineErrorRate(),
            request.getEquipmentEtaMaint(),
            request.getEquipmentIdx(),
            request.getEquipmentBaselineCalculatedAt()
        );

        return EquipmentBaselineUpdateResponse.builder()
            .equipmentBaselineId(equipmentBaseline.getEquipmentBaselineId())
            .equipmentId(equipmentBaseline.getEquipmentId())
            .equipmentAgingParamId(equipmentBaseline.getEquipmentAgingParamId())
            .equipmentStandardPerformanceRate(equipmentBaseline.getEquipmentStandardPerformanceRate())
            .equipmentBaselineErrorRate(equipmentBaseline.getEquipmentBaselineErrorRate())
            .equipmentEtaMaint(equipmentBaseline.getEquipmentEtaMaint())
            .equipmentIdx(equipmentBaseline.getEquipmentIdx())
            .equipmentBaselineCalculatedAt(equipmentBaseline.getEquipmentBaselineCalculatedAt())
            .build();
    }
}
