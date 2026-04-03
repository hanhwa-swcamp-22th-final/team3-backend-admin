package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EnvironmentStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EnvironmentStandardQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnvironmentStandardQueryService {

    private final EnvironmentStandardQueryMapper environmentStandardQueryMapper;

    public List<EnvironmentStandardQueryResponse> getEnvironmentStandardList(EnvironmentStandardSearchRequest request) {
        return environmentStandardQueryMapper.selectEnvironmentStandardList(request);
    }

    public EnvironmentStandardDetailResponse getEnvironmentStandardDetail(Long environmentStandardId) {
        EnvironmentStandardDetailResponse response = environmentStandardQueryMapper.selectEnvironmentStandardDetailById(environmentStandardId);
        if (response == null) {
            throw new BusinessException(ErrorCode.ENVIRONMENT_STANDARD_NOT_FOUND);
        }
        return response;
    }

    public boolean existsByEnvironmentCode(String environmentCode) {
        return environmentStandardQueryMapper.selectEnvironmentStandardIdByCode(environmentCode) != null;
    }
}
