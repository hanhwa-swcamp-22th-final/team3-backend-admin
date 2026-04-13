package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EquipmentSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentEnumOptionResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentEnumValuesResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentLatestSnapshotQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentSummaryQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EquipmentQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentQueryService {

    private final EquipmentQueryMapper equipmentQueryMapper;

    public EquipmentEnumValuesResponse getEquipmentEnumValues() {
        return new EquipmentEnumValuesResponse(
            toOptions(equipmentQueryMapper.selectEquipmentStatusColumnType(), this::equipmentStatusLabel),
            toOptions(equipmentQueryMapper.selectEquipmentGradeColumnType(), value -> value),
            toOptions(equipmentQueryMapper.selectEnvironmentTypeColumnType(), this::environmentTypeLabel)
        );
    }

    private List<EquipmentEnumOptionResponse> toOptions(String columnType, Function<String, String> labelResolver) {
        if (columnType == null || !columnType.startsWith("enum(")) {
            return List.of();
        }

        Matcher matcher = Pattern.compile("'((?:''|[^'])*)'").matcher(columnType);
        List<EquipmentEnumOptionResponse> options = new ArrayList<>();
        while (matcher.find()) {
            String value = matcher.group(1).replace("''", "'");
            options.add(new EquipmentEnumOptionResponse(value, labelResolver.apply(value)));
        }
        return options;
    }

    private String equipmentStatusLabel(String status) {
        return switch (status) {
            case "OPERATING" -> "가동 중";
            case "STOPPED" -> "중지";
            case "UNDER_INSPECTION" -> "점검 중";
            case "DISPOSED" -> "폐기";
            default -> status;
        };
    }

    private String environmentTypeLabel(String type) {
        return switch (type) {
            case "DRYROOM" -> "드라이룸";
            case "CLEANROOM" -> "클린룸";
            case "GENERAL" -> "일반 환경";
            default -> type;
        };
    }

    /**
     * 설비 목록을 조회한다.
     * @param request 조회 조건 정보
     * @return 설비 목록 응답
     */
    public List<EquipmentQueryResponse> getEquipmentList(EquipmentSearchRequest request) {
        return equipmentQueryMapper.selectEquipmentList(request);
    }

    /**
     * 설비 상태 집계 정보를 조회한다.
     * @return 설비 상태 집계 응답
     */
    public EquipmentSummaryQueryResponse getEquipmentSummary() {
        return equipmentQueryMapper.selectEquipmentSummary();
    }

    /**
     * 설비 목록과 각 설비의 최신 aging/baseline/event/maintenance 요약 정보를 함께 조회한다.
     * @param request 조회 조건 정보
     * @return 최신 스냅샷이 포함된 설비 목록 응답
     */
    public List<EquipmentLatestSnapshotQueryResponse> getEquipmentListWithLatestSnapshots(EquipmentSearchRequest request) {
        return equipmentQueryMapper.selectEquipmentListWithLatestSnapshots(request);
    }

    /**
     * 설비 상세 정보를 조회한다.
     * @param equipmentId 조회할 설비 ID
     * @return 설비 상세 응답
     */
    public EquipmentDetailResponse getEquipmentDetail(Long equipmentId) {
        EquipmentDetailResponse response = equipmentQueryMapper.selectEquipmentDetailById(equipmentId);
        if (response == null) {
            throw new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND);
        }
        return response;
    }

    /**
     * 설비 코드 존재 여부를 조회한다.
     * @param equipmentCode 확인할 설비 코드
     * @return 설비 코드 존재 여부
     */
    public boolean existsByEquipmentCode(String equipmentCode) {
        return equipmentQueryMapper.selectEquipmentIdByCode(equipmentCode) != null;
    }

    public Long getEquipmentAgingParamIdByEquipmentId(Long equipmentId) {
        return equipmentQueryMapper.selectEquipmentAgingParamIdByEquipmentId(equipmentId);
    }

    public Long getEquipmentBaselineIdByEquipmentId(Long equipmentId) {
        return equipmentQueryMapper.selectEquipmentBaselineIdByEquipmentId(equipmentId);
    }
}
