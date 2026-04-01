package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentAgingParamDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EquipmentQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentAgingParamQueryService {

    private final EquipmentQueryMapper equipmentQueryMapper;

    /**
     * 설비 노후 파라미터 상세 정보를 조회한다.
     * @param equipmentAgingParamId 조회할 설비 노후 파라미터 식별자
     * @return 설비 노후 파라미터 상세 정보
     */
    public EquipmentAgingParamDetailResponse getEquipmentAgingParamDetail(Long equipmentAgingParamId) {
        EquipmentAgingParamDetailResponse response =
            equipmentQueryMapper.selectEquipmentAgingParamDetailById(equipmentAgingParamId);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_AGING_PARAM_NOT_FOUND);
        }
        return response;
    }
}
