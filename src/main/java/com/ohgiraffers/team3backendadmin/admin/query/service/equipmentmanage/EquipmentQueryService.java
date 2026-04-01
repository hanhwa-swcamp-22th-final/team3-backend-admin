package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentLatestSnapshotQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EquipmentQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentQueryService {

    private final EquipmentQueryMapper equipmentQueryMapper;

    /**
     * 설비 목록을 조회한다.
     * @param request 조회 조건 정보
     * @return 설비 목록 응답
     */
    public List<EquipmentQueryResponse> getEquipmentList(EquipmentSearchRequest request) {
        return equipmentQueryMapper.selectEquipmentList(request);
    }

    /**
     * 설비 목록과 각 설비의 최신 aging/baseline/event/maintenance 요약 정보를 함께 조회한다.
     * @param request 조회 조건 정보
     * @return 최신 스냅샷이 포함된 설비 목록 응답
     */
    public List<EquipmentLatestSnapshotQueryResponse> getEquipmentListWithLatestSnapshots(EquipmentSearchRequest request) {
        return equipmentQueryMapper.selectEquipmentListWithLatestSnapshots(request);
    }

    /**
     * 설비 상세 정보를 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @return 설비 상세 응답
     */
    public EquipmentDetailResponse getEquipmentDetail(Long equipmentId) {
        EquipmentDetailResponse response = equipmentQueryMapper.selectEquipmentDetailById(equipmentId);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND);
        }
        return response;
    }

    /**
     * 설비 코드 존재 여부를 조회한다.
     * @param equipmentCode 확인할 설비 코드
     * @return 설비 코드 존재 여부
     */
    public boolean existsByEquipmentCode(String equipmentCode) {
        return equipmentQueryMapper.selectEquipmentIdByCode(equipmentCode) != null;
    }

    public Long getEquipmentAgingParamIdByEquipmentId(Long equipmentId) {
        return equipmentQueryMapper.selectEquipmentAgingParamIdByEquipmentId(equipmentId);
    }

    public Long getEquipmentBaselineIdByEquipmentId(Long equipmentId) {
        return equipmentQueryMapper.selectEquipmentBaselineIdByEquipmentId(equipmentId);
    }
}
