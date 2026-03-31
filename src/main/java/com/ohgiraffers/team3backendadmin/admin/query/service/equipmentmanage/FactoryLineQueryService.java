package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.FactoryLineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.FactoryLineQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FactoryLineQueryService {

    private final FactoryLineQueryMapper factoryLineQueryMapper;

    public List<FactoryLineQueryResponse> getFactoryLineList(FactoryLineSearchRequest request) {
        return factoryLineQueryMapper.selectFactoryLineList(request);
    }

    public FactoryLineDetailResponse getFactoryLineDetail(Long factoryLineId) {
        FactoryLineDetailResponse response = factoryLineQueryMapper.selectFactoryLineDetailById(factoryLineId);
        if (response == null) {
            throw new BusinessException(ErrorCode.FACTORY_LINE_NOT_FOUND);
        }
        return response;
    }

    public boolean existsByFactoryLineCode(String factoryLineCode) {
        return factoryLineQueryMapper.selectFactoryLineIdByCode(factoryLineCode) != null;
    }
}
