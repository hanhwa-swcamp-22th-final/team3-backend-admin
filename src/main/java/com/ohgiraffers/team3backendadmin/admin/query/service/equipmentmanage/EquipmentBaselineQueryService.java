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

    /**
     * 설비 baseline 이력 목록을 조회한다.
     * @param request 설비 ID 및 계산 시각 조건
     * @return 설비 baseline 이력 목록
     */
    public List<EquipmentBaselineDetailResponse> getEquipmentBaselineHistory(EquipmentBaselineSearchRequest request) {
        return equipmentQueryMapper.selectEquipmentBaselineHistory(request);
    }

    /**
     * 특정 설비의 최신 baseline 1건을 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @return 최신 설비 baseline 상세 응답
     */
    public EquipmentBaselineDetailResponse getLatestEquipmentBaseline(Long equipmentId) {
        validateEquipmentId(equipmentId);
        EquipmentBaselineDetailResponse response = equipmentQueryMapper.selectLatestEquipmentBaselineByEquipmentId(equipmentId);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_BASELINE_NOT_FOUND);
        }
        return response;
    }

    /**
     * 특정 시점 이전 또는 동일 시점 기준으로 가장 최근의 baseline 을 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @param referenceTime 기준 시각
     * @return 기준 시각 이전 최신 설비 baseline 상세 응답
     */
    public EquipmentBaselineDetailResponse getLatestEquipmentBaselineBeforeOrAt(Long equipmentId, LocalDateTime referenceTime) {
        validateEquipmentId(equipmentId);
        validateReferenceTime(referenceTime);
        EquipmentBaselineDetailResponse response = equipmentQueryMapper.selectLatestEquipmentBaselineBeforeOrAt(equipmentId, referenceTime);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_BASELINE_NOT_FOUND);
        }
        return response;
    }

    /**
     * 특정 시점 이후 또는 동일 시점 기준으로 가장 먼저 계산된 baseline 을 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @param referenceTime 기준 시각
     * @return 기준 시각 이후 최초 설비 baseline 상세 응답
     */
    public EquipmentBaselineDetailResponse getFirstEquipmentBaselineAfterOrAt(Long equipmentId, LocalDateTime referenceTime) {
        validateEquipmentId(equipmentId);
        validateReferenceTime(referenceTime);
        EquipmentBaselineDetailResponse response = equipmentQueryMapper.selectFirstEquipmentBaselineAfterOrAt(equipmentId, referenceTime);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_BASELINE_NOT_FOUND);
        }
        return response;
    }

    /**
     * 계산이 완료되지 않은 설비 baseline 목록을 조회한다.
     * @return 미계산 설비 baseline 목록
     */
    public List<EquipmentBaselineDetailResponse> getUncalculatedEquipmentBaselineList() {
        return equipmentQueryMapper.selectUncalculatedEquipmentBaselineList();
    }

    private void validateEquipmentId(Long equipmentId) {
        if (equipmentId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "equipmentId is required.");
        }
    }

    private void validateReferenceTime(LocalDateTime referenceTime) {
        if (referenceTime == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "referenceTime is required.");
        }
    }
}
