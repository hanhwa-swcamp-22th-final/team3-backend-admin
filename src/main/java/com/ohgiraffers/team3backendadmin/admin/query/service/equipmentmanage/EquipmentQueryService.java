package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EquipmentQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentQueryService {

    private final EquipmentQueryMapper equipmentQueryMapper;

    /**
     * 검색 조건에 맞는 설비 목록을 조회한다.
     * @param request 공정, 상태, 등급, 키워드를 포함한 조회 조건 정보
     * @return 조회 조건에 맞는 설비 목록
     */
    public List<EquipmentQueryResponse> getEquipmentList(EquipmentSearchRequest request) {
        return equipmentQueryMapper.selectEquipmentList(request);
    }

    /**
     * 특정 설비의 상세 정보를 조회한다.
     * @param equipmentId 조회할 설비의 식별자
     * @return 조회된 설비의 상세 정보
     */
    public EquipmentDetailResponse getEquipmentDetail(Long equipmentId) {
        return equipmentQueryMapper.selectEquipmentDetailById(equipmentId);
    }

    /**
     * 동일한 설비 코드가 이미 사용 중인지 확인한다.
     * @param equipmentCode 중복 여부를 확인할 설비 코드
     * @return 사용 중이면 true, 아니면 false
     */
    public boolean existsByEquipmentCode(String equipmentCode) {
        return equipmentQueryMapper.selectEquipmentIdByCode(equipmentCode) != null;
    }

    /**
     * 특정 설비에 연결된 노후도 파라미터 식별자를 조회한다.
     * @param equipmentId 조회할 설비의 식별자
     * @return 연결된 노후도 파라미터 식별자
     */
    public Long getEquipmentAgingParamIdByEquipmentId(Long equipmentId) {
        return equipmentQueryMapper.selectEquipmentAgingParamIdByEquipmentId(equipmentId);
    }

    /**
     * 특정 설비에 연결된 베이스라인 식별자를 조회한다.
     * @param equipmentId 조회할 설비의 식별자
     * @return 연결된 베이스라인 식별자
     */
    public Long getEquipmentBaselineIdByEquipmentId(Long equipmentId) {
        return equipmentQueryMapper.selectEquipmentBaselineIdByEquipmentId(equipmentId);
    }
}