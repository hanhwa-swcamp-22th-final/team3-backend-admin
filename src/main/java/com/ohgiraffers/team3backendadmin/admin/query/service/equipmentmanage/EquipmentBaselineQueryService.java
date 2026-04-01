package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentBaselineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EquipmentQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentBaselineQueryService {

    private final EquipmentQueryMapper equipmentQueryMapper;

    /**
     * 설비 baseline 상세 정보를 조회한다.
     * @param equipmentBaselineId 조회할 설비 baseline 식별자
     * @return 설비 baseline 상세 정보
     */
    public EquipmentBaselineDetailResponse getEquipmentBaselineDetail(Long equipmentBaselineId) {
        EquipmentBaselineDetailResponse response =
            equipmentQueryMapper.selectEquipmentBaselineDetailById(equipmentBaselineId);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_BASELINE_NOT_FOUND);
        }
        return response;
    }
}
