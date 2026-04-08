package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.FactoryLineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.FactoryLineEquipmentStatsResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.FactoryLineQueryResponse;
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

    /**
     * 생산 라인별 설비 통계 정보를 조회한다.
     * @param factoryLineId 조회할 생산 라인 ID
     * @return 생산 라인 설비 통계 응답
     */
    public FactoryLineEquipmentStatsResponse getFactoryLineEquipmentStats(Long factoryLineId) {
        FactoryLineEquipmentStatsResponse response = factoryLineQueryMapper.selectFactoryLineEquipmentStats(factoryLineId);
        if (response == null) {
            throw new BusinessException(ErrorCode.FACTORY_LINE_NOT_FOUND);
        }
        return response;
    }

    public boolean existsByFactoryLineCode(String factoryLineCode) {
        return factoryLineQueryMapper.selectFactoryLineIdByCode(factoryLineCode) != null;
    }
}
