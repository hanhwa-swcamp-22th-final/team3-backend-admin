package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EnvironmentEventSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentEventDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentEventQueryResponse;
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

    /**
     * 환경 이벤트 상세 정보를 조회한다.
     * @param environmentEventId 조회할 환경 이벤트 ID
     * @return 환경 이벤트 상세 응답
     */
    public EnvironmentEventDetailResponse getEnvironmentEventDetail(Long environmentEventId) {
        EnvironmentEventDetailResponse response = environmentEventQueryMapper.selectEnvironmentEventDetailById(environmentEventId);
        if (response == null) {
            throw new BusinessException(ErrorCode.ENVIRONMENT_EVENT_NOT_FOUND);
        }
        return response;
    }

    /**
     * 특정 설비의 최신 환경 이벤트 1건을 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @return 최신 환경 이벤트 상세 응답
     */
    public EnvironmentEventDetailResponse getLatestEnvironmentEvent(Long equipmentId) {
        validateEquipmentId(equipmentId);
        EnvironmentEventDetailResponse response = environmentEventQueryMapper.selectLatestEnvironmentEventByEquipmentId(equipmentId);
        if (response == null) {
            throw new BusinessException(ErrorCode.ENVIRONMENT_EVENT_NOT_FOUND);
        }
        return response;
    }

    /**
     * 특정 시점 이전 또는 동일 시점 기준으로 가장 최근의 환경 이벤트를 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @param referenceTime 기준 시각
     * @return 기준 시각 이전 최신 환경 이벤트 상세 응답
     */
    public EnvironmentEventDetailResponse getLatestEnvironmentEventBeforeOrAt(Long equipmentId, LocalDateTime referenceTime) {
        validateEquipmentId(equipmentId);
        validateReferenceTime(referenceTime);
        EnvironmentEventDetailResponse response = environmentEventQueryMapper.selectLatestEnvironmentEventBeforeOrAt(equipmentId, referenceTime);
        if (response == null) {
            throw new BusinessException(ErrorCode.ENVIRONMENT_EVENT_NOT_FOUND);
        }
        return response;
    }

    /**
     * 특정 시점 이후 또는 동일 시점 기준으로 가장 먼저 발생한 환경 이벤트를 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @param referenceTime 기준 시각
     * @return 기준 시각 이후 최초 환경 이벤트 상세 응답
     */
    public EnvironmentEventDetailResponse getFirstEnvironmentEventAfterOrAt(Long equipmentId, LocalDateTime referenceTime) {
        validateEquipmentId(equipmentId);
        validateReferenceTime(referenceTime);
        EnvironmentEventDetailResponse response = environmentEventQueryMapper.selectFirstEnvironmentEventAfterOrAt(equipmentId, referenceTime);
        if (response == null) {
            throw new BusinessException(ErrorCode.ENVIRONMENT_EVENT_NOT_FOUND);
        }
        return response;
    }

    /**
     * 아직 보정이 적용되지 않은 환경 이벤트 목록을 조회한다.
     * @return 미해결 환경 이벤트 목록
     */
    public List<EnvironmentEventQueryResponse> getUnresolvedEnvironmentEventList() {
        return environmentEventQueryMapper.selectUnresolvedEnvironmentEventList();
    }

    private void validateEquipmentId(Long equipmentId) {
        if (equipmentId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "equipmentId is required.");
        }
    }

    private void validateReferenceTime(LocalDateTime referenceTime) {
        if (referenceTime == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "referenceTime is required.");
        }
    }
}
