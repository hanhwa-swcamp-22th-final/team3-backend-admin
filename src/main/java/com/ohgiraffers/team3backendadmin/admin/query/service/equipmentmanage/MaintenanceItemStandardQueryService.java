package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.MaintenanceItemStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceItemStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceItemStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.MaintenanceItemStandardQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceItemStandardQueryService {

    private final MaintenanceItemStandardQueryMapper maintenanceItemStandardQueryMapper;

    public List<MaintenanceItemStandardQueryResponse> getMaintenanceItemStandardList(MaintenanceItemStandardSearchRequest request) {
        return maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardList(request);
    }

    public MaintenanceItemStandardDetailResponse getMaintenanceItemStandardDetail(Long maintenanceItemStandardId) {
        MaintenanceItemStandardDetailResponse response = maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardDetailById(maintenanceItemStandardId);
        if (response == null) {
            throw new BusinessException(ErrorCode.MAINTENANCE_ITEM_STANDARD_NOT_FOUND);
        }
        return response;
    }
}