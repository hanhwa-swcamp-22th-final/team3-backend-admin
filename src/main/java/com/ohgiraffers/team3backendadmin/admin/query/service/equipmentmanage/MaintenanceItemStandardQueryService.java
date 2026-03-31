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

    /**
     * 유지보수 항목 기준 목록을 조회한다.
     * @param request 조회 조건 정보
     * @return 유지보수 항목 기준 목록
     */
    public List<MaintenanceItemStandardQueryResponse> getMaintenanceItemStandardList(MaintenanceItemStandardSearchRequest request) {
        return maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardList(request);
    }

    /**
     * 유지보수 항목 기준 상세를 조회한다.
     * @param maintenanceItemStandardId 조회할 유지보수 항목 기준 ID
     * @return 유지보수 항목 기준 상세 응답
     */
    public MaintenanceItemStandardDetailResponse getMaintenanceItemStandardDetail(Long maintenanceItemStandardId) {
        MaintenanceItemStandardDetailResponse response = maintenanceItemStandardQueryMapper.selectMaintenanceItemStandardDetailById(maintenanceItemStandardId);
        if (response == null) {
            throw new BusinessException(ErrorCode.MAINTENANCE_ITEM_STANDARD_NOT_FOUND);
        }
        return response;
    }
}
