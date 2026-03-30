package com.ohgiraffers.team3backendadmin.admin.query.service;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EquipmentQueryMapper;
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

    public EquipmentDetailResponse getEquipmentDetail(Long equipmentId) {
        return equipmentQueryMapper.selectEquipmentDetailById(equipmentId);
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
