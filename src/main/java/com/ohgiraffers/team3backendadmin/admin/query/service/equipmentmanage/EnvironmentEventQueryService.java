package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EnvironmentEventSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EnvironmentEventQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnvironmentEventQueryService {

    private final EnvironmentEventQueryMapper environmentEventQueryMapper;

    public List<EnvironmentEventQueryResponse> getEnvironmentEventList(EnvironmentEventSearchRequest request) {
        return environmentEventQueryMapper.selectEnvironmentEventList(request);
    }

    public EnvironmentEventDetailResponse getEnvironmentEventDetail(Long environmentEventId) {
        EnvironmentEventDetailResponse response = environmentEventQueryMapper.selectEnvironmentEventDetailById(environmentEventId);
        if (response == null) {
            throw new BusinessException(ErrorCode.ENVIRONMENT_EVENT_NOT_FOUND);
        }
        return response;
    }

    public EnvironmentEventDetailResponse getLatestEnvironmentEvent(Long equipmentId) {
        EnvironmentEventDetailResponse response = environmentEventQueryMapper.selectLatestEnvironmentEventByEquipmentId(equipmentId);
        if (response == null) {
            throw new BusinessException(ErrorCode.ENVIRONMENT_EVENT_NOT_FOUND);
        }
        return response;
    }

    public EnvironmentEventDetailResponse getLatestEnvironmentEventBeforeOrAt(Long equipmentId, LocalDateTime referenceTime) {
        EnvironmentEventDetailResponse response = environmentEventQueryMapper.selectLatestEnvironmentEventBeforeOrAt(equipmentId, referenceTime);
        if (response == null) {
            throw new BusinessException(ErrorCode.ENVIRONMENT_EVENT_NOT_FOUND);
        }
        return response;
    }

    public EnvironmentEventDetailResponse getFirstEnvironmentEventAfterOrAt(Long equipmentId, LocalDateTime referenceTime) {
        EnvironmentEventDetailResponse response = environmentEventQueryMapper.selectFirstEnvironmentEventAfterOrAt(equipmentId, referenceTime);
        if (response == null) {
            throw new BusinessException(ErrorCode.ENVIRONMENT_EVENT_NOT_FOUND);
        }
        return response;
    }

    public List<EnvironmentEventQueryResponse> getUnresolvedEnvironmentEventList() {
        return environmentEventQueryMapper.selectUnresolvedEnvironmentEventList();
    }
}
