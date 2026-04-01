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

    public List<EquipmentQueryResponse> getEquipmentList(EquipmentSearchRequest request) {
        return equipmentQueryMapper.selectEquipmentList(request);
    }

    public List<EquipmentLatestSnapshotQueryResponse> getEquipmentListWithLatestSnapshots(EquipmentSearchRequest request) {
        return equipmentQueryMapper.selectEquipmentListWithLatestSnapshots(request);
    }

    public EquipmentDetailResponse getEquipmentDetail(Long equipmentId) {
        EquipmentDetailResponse response = equipmentQueryMapper.selectEquipmentDetailById(equipmentId);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND);
        }
        return response;
    }

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
