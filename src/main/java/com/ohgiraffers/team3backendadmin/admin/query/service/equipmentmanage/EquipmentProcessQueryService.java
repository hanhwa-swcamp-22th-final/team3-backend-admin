package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentProcessSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentProcessQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EquipmentProcessQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentProcessQueryService {

    private final EquipmentProcessQueryMapper equipmentProcessQueryMapper;

    /**
     * 검색 조건에 맞는 공정 목록을 조회한다.
     * @param request 생산 라인 식별자와 키워드를 포함한 조회 조건 정보
     * @return 조회 조건에 맞는 공정 목록
     */
    public List<EquipmentProcessQueryResponse> getEquipmentProcessList(EquipmentProcessSearchRequest request) {
        return equipmentProcessQueryMapper.selectEquipmentProcessList(request);
    }

    /**
     * 특정 공정의 상세 정보를 조회한다.
     * @param equipmentProcessId 조회할 공정의 식별자
     * @return 조회된 공정의 상세 정보
     */
    public EquipmentProcessDetailResponse getEquipmentProcessDetail(Long equipmentProcessId) {
        return equipmentProcessQueryMapper.selectEquipmentProcessDetailById(equipmentProcessId);
    }

    /**
     * 동일한 공정 코드가 이미 사용 중인지 확인한다.
     * @param equipmentProcessCode 중복 여부를 확인할 공정 코드
     * @return 사용 중이면 true, 아니면 false
     */
    public boolean existsByEquipmentProcessCode(String equipmentProcessCode) {
        return equipmentProcessQueryMapper.selectEquipmentProcessIdByCode(equipmentProcessCode) != null;
    }
}