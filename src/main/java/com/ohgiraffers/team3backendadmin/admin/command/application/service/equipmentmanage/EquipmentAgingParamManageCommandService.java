package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.equipmentmanage.EquipmentAgingParamUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.equipmentmanage.EquipmentAgingParamUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentAgingParam;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentAgingParamRepository;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentAgingParamManageCommandService {

    private final EquipmentAgingParamRepository equipmentAgingParamRepository;
    private final EquipmentReferenceSnapshotCommandService equipmentReferenceSnapshotCommandService;

    /**
     * 설비 노후 파라미터와 계산 결과를 함께 수정한다.
     * @param equipmentAgingParamId 수정할 설비 노후 파라미터 식별자
     * @param request 보증 기간, 설계 수명, 마모 계수 및 계산 결과 정보
     * @return 수정된 설비 노후 파라미터 정보
     */
    public EquipmentAgingParamUpdateResponse updateEquipmentAgingParam(Long equipmentAgingParamId,
                                                                       EquipmentAgingParamUpdateRequest request) {
        EquipmentAgingParam equipmentAgingParam = equipmentAgingParamRepository.findById(equipmentAgingParamId)
            .orElseThrow(() -> new BusinessException(ErrorCode.EQUIPMENT_AGING_PARAM_NOT_FOUND));

        equipmentAgingParam.update(
            request.getEquipmentWarrantyMonth(),
            request.getEquipmentDesignLifeMonths(),
            request.getEquipmentWearCoefficient()
        );
        equipmentAgingParam.updateCalculationInfo(
            request.getEquipmentEtaAge(),
            request.getEquipmentAgeMonths(),
            request.getEquipmentAgeCalculatedAt()
        );
        equipmentReferenceSnapshotCommandService.publishSnapshotAfterCommit(equipmentAgingParam.getEquipmentId());

        return EquipmentAgingParamUpdateResponse.builder()
            .equipmentAgingParamId(equipmentAgingParam.getEquipmentAgingParamId())
            .equipmentId(equipmentAgingParam.getEquipmentId())
            .equipmentEtaAge(equipmentAgingParam.getEquipmentEtaAge())
            .equipmentWarrantyMonth(equipmentAgingParam.getEquipmentWarrantyMonth())
            .equipmentDesignLifeMonths(equipmentAgingParam.getEquipmentDesignLifeMonths())
            .equipmentWearCoefficient(equipmentAgingParam.getEquipmentWearCoefficient())
            .equipmentAgeMonths(equipmentAgingParam.getEquipmentAgeMonths())
            .equipmentAgeCalculatedAt(equipmentAgingParam.getEquipmentAgeCalculatedAt())
            .build();
    }
}
