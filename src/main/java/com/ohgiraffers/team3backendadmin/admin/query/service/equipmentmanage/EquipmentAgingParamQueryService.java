package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EquipmentAgingParamSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentAgingParamDetailResponse;
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

    /**
     * 설비 노후 파라미터 이력 목록을 조회한다.
     * @param request 설비 ID 및 계산 시각 조건
     * @return 설비 노후 파라미터 이력 목록
     */
    public List<EquipmentAgingParamDetailResponse> getEquipmentAgingParamHistory(EquipmentAgingParamSearchRequest request) {
        return equipmentQueryMapper.selectEquipmentAgingParamHistory(request);
    }

    /**
     * 특정 설비의 최신 노후 파라미터 1건을 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @return 최신 설비 노후 파라미터 상세 응답
     */
    public EquipmentAgingParamDetailResponse getLatestEquipmentAgingParam(Long equipmentId) {
        validateEquipmentId(equipmentId);
        EquipmentAgingParamDetailResponse response = equipmentQueryMapper.selectLatestEquipmentAgingParamByEquipmentId(equipmentId);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_AGING_PARAM_NOT_FOUND);
        }
        return response;
    }

    /**
     * 특정 시점 이전 또는 동일 시점 기준으로 가장 최근의 노후 파라미터를 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @param referenceTime 기준 시각
     * @return 기준 시각 이전 최신 설비 노후 파라미터 상세 응답
     */
    public EquipmentAgingParamDetailResponse getLatestEquipmentAgingParamBeforeOrAt(Long equipmentId, LocalDateTime referenceTime) {
        validateEquipmentId(equipmentId);
        validateReferenceTime(referenceTime);
        EquipmentAgingParamDetailResponse response = equipmentQueryMapper.selectLatestEquipmentAgingParamBeforeOrAt(equipmentId, referenceTime);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_AGING_PARAM_NOT_FOUND);
        }
        return response;
    }

    /**
     * 특정 시점 이후 또는 동일 시점 기준으로 가장 먼저 계산된 노후 파라미터를 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @param referenceTime 기준 시각
     * @return 기준 시각 이후 최초 설비 노후 파라미터 상세 응답
     */
    public EquipmentAgingParamDetailResponse getFirstEquipmentAgingParamAfterOrAt(Long equipmentId, LocalDateTime referenceTime) {
        validateEquipmentId(equipmentId);
        validateReferenceTime(referenceTime);
        EquipmentAgingParamDetailResponse response = equipmentQueryMapper.selectFirstEquipmentAgingParamAfterOrAt(equipmentId, referenceTime);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_AGING_PARAM_NOT_FOUND);
        }
        return response;
    }

    /**
     * 계산이 완료되지 않은 설비 노후 파라미터 목록을 조회한다.
     * @return 미계산 설비 노후 파라미터 목록
     */
    public List<EquipmentAgingParamDetailResponse> getUncalculatedEquipmentAgingParamList() {
        return equipmentQueryMapper.selectUncalculatedEquipmentAgingParamList();
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
