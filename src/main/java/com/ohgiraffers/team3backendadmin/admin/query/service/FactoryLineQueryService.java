package com.ohgiraffers.team3backendadmin.admin.query.service;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.FactoryLineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.FactoryLineQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FactoryLineQueryService {

    private final FactoryLineQueryMapper factoryLineQueryMapper;

    /**
     * 검색 조건에 맞는 생산 라인 목록을 조회한다.
     * @param request 키워드 등 목록 조회 조건 값
     * @return 조회 조건에 맞는 생산 라인 목록
     */
    public List<FactoryLineQueryResponse> getFactoryLineList(FactoryLineSearchRequest request) {
        return factoryLineQueryMapper.selectFactoryLineList(request);
    }

    /**
     * 특정 생산 라인의 상세 정보를 조회한다.
     * @param factoryLineId 조회 대상 생산 라인의 식별자
     * @return 조회된 생산 라인의 상세 정보
     */
    public FactoryLineDetailResponse getFactoryLineDetail(Long factoryLineId) {
        return factoryLineQueryMapper.selectFactoryLineDetailById(factoryLineId);
    }

    /**
     * 동일한 생산 라인 코드가 이미 사용 중인지 확인한다.
     * @param factoryLineCode 중복 여부를 확인할 생산 라인 코드
     * @return 사용 중이면 true, 아니면 false
     */
    public boolean existsByFactoryLineCode(String factoryLineCode) {
        return factoryLineQueryMapper.selectFactoryLineIdByCode(factoryLineCode) != null;
    }
}
