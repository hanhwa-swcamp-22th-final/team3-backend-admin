package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentProcessSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EquipmentProcessQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentProcessQueryService {

    private final EquipmentProcessQueryMapper equipmentProcessQueryMapper;

    public List<EquipmentProcessQueryResponse> getEquipmentProcessList(EquipmentProcessSearchRequest request) {
        return equipmentProcessQueryMapper.selectEquipmentProcessList(request);
    }

    public EquipmentProcessDetailResponse getEquipmentProcessDetail(Long equipmentProcessId) {
        EquipmentProcessDetailResponse response = equipmentProcessQueryMapper.selectEquipmentProcessDetailById(equipmentProcessId);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_PROCESS_NOT_FOUND);
        }
        return response;
    }

    public boolean existsByEquipmentProcessCode(String equipmentProcessCode) {
        return equipmentProcessQueryMapper.selectEquipmentProcessIdByCode(equipmentProcessCode) != null;
    }
}
