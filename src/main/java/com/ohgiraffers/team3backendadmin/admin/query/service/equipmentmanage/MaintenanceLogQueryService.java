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
import java.time.LocalDate;

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

    /**
     * 특정 설비의 최신 유지보수 이력 1건을 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @return 최신 유지보수 이력 상세 응답
     */
    public MaintenanceLogDetailResponse getLatestMaintenanceLog(Long equipmentId) {
        validateEquipmentId(equipmentId);
        MaintenanceLogDetailResponse response = maintenanceLogQueryMapper.selectLatestMaintenanceLogByEquipmentId(equipmentId);
        if (response == null) {
            throw new BusinessException(ErrorCode.MAINTENANCE_LOG_NOT_FOUND);
        }
        return response;
    }

    /**
     * 특정 일자 이전 또는 동일 일자 기준으로 가장 최근의 유지보수 이력을 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @param referenceDate 기준 일자
     * @return 기준 일자 이전 최신 유지보수 이력 상세 응답
     */
    public MaintenanceLogDetailResponse getLatestMaintenanceLogBeforeOrAt(Long equipmentId, LocalDate referenceDate) {
        validateEquipmentId(equipmentId);
        validateReferenceDate(referenceDate);
        MaintenanceLogDetailResponse response = maintenanceLogQueryMapper.selectLatestMaintenanceLogBeforeOrAt(equipmentId, referenceDate);
        if (response == null) {
            throw new BusinessException(ErrorCode.MAINTENANCE_LOG_NOT_FOUND);
        }
        return response;
    }

    /**
     * 특정 일자 이후 또는 동일 일자 기준으로 가장 먼저 발생한 유지보수 이력을 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @param referenceDate 기준 일자
     * @return 기준 일자 이후 최초 유지보수 이력 상세 응답
     */
    public MaintenanceLogDetailResponse getFirstMaintenanceLogAfterOrAt(Long equipmentId, LocalDate referenceDate) {
        validateEquipmentId(equipmentId);
        validateReferenceDate(referenceDate);
        MaintenanceLogDetailResponse response = maintenanceLogQueryMapper.selectFirstMaintenanceLogAfterOrAt(equipmentId, referenceDate);
        if (response == null) {
            throw new BusinessException(ErrorCode.MAINTENANCE_LOG_NOT_FOUND);
        }
        return response;
    }

    /**
     * 비정상 결과이거나 점수/예상 정비 차이가 누락된 유지보수 이력 목록을 조회한다.
     * @return 이상치 또는 미완성 유지보수 이력 목록
     */
    public List<MaintenanceLogQueryResponse> getAbnormalOrIncompleteMaintenanceLogList() {
        return maintenanceLogQueryMapper.selectAbnormalOrIncompleteMaintenanceLogList();
    }

    private void validateEquipmentId(Long equipmentId) {
        if (equipmentId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "equipmentId is required.");
        }
    }

    private void validateReferenceDate(LocalDate referenceDate) {
        if (referenceDate == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "referenceDate is required.");
        }
    }
}
