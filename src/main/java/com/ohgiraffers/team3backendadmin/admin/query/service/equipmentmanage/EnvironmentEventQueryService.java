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
}
