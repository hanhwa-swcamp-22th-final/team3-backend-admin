package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentBaselineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentBaselineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EquipmentQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<EquipmentBaselineDetailResponse> getEquipmentBaselineHistory(EquipmentBaselineSearchRequest request) {
        return equipmentQueryMapper.selectEquipmentBaselineHistory(request);
    }

    public EquipmentBaselineDetailResponse getLatestEquipmentBaseline(Long equipmentId) {
        EquipmentBaselineDetailResponse response = equipmentQueryMapper.selectLatestEquipmentBaselineByEquipmentId(equipmentId);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_BASELINE_NOT_FOUND);
        }
        return response;
    }

    public EquipmentBaselineDetailResponse getLatestEquipmentBaselineBeforeOrAt(Long equipmentId, LocalDateTime referenceTime) {
        EquipmentBaselineDetailResponse response = equipmentQueryMapper.selectLatestEquipmentBaselineBeforeOrAt(equipmentId, referenceTime);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_BASELINE_NOT_FOUND);
        }
        return response;
    }

    public EquipmentBaselineDetailResponse getFirstEquipmentBaselineAfterOrAt(Long equipmentId, LocalDateTime referenceTime) {
        EquipmentBaselineDetailResponse response = equipmentQueryMapper.selectFirstEquipmentBaselineAfterOrAt(equipmentId, referenceTime);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_BASELINE_NOT_FOUND);
        }
        return response;
    }

    public List<EquipmentBaselineDetailResponse> getUncalculatedEquipmentBaselineList() {
        return equipmentQueryMapper.selectUncalculatedEquipmentBaselineList();
    }
}
