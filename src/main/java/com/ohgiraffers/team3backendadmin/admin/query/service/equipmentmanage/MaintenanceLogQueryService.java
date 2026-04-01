package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.MaintenanceLogSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceLogDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceLogQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.MaintenanceLogQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaintenanceLogQueryService {

    private final MaintenanceLogQueryMapper maintenanceLogQueryMapper;

    /**
     * 유지보수 이력 목록을 조회한다.
     * @param request 조회 조건 정보
     * @return 유지보수 이력 목록
     */
    public List<MaintenanceLogQueryResponse> getMaintenanceLogList(MaintenanceLogSearchRequest request) {
        return maintenanceLogQueryMapper.selectMaintenanceLogList(request);
    }

    /**
     * 유지보수 이력 상세를 조회한다.
     * @param maintenanceLogId 조회할 유지보수 이력 ID
     * @return 유지보수 이력 상세 응답
     */
    public MaintenanceLogDetailResponse getMaintenanceLogDetail(Long maintenanceLogId) {
        MaintenanceLogDetailResponse response = maintenanceLogQueryMapper.selectMaintenanceLogDetailById(maintenanceLogId);
        if (response == null) {
            throw new BusinessException(ErrorCode.MAINTENANCE_LOG_NOT_FOUND);
        }
        return response;
    }
}
