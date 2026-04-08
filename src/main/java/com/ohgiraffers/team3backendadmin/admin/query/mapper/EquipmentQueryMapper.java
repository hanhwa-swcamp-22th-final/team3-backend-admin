package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EquipmentAgingParamSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EquipmentBaselineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EquipmentSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentAgingParamDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentBaselineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentLatestSnapshotQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentSummaryQueryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EquipmentQueryMapper {

    List<EquipmentQueryResponse> selectEquipmentList(@Param("request") EquipmentSearchRequest request);

    EquipmentSummaryQueryResponse selectEquipmentSummary();

    List<EquipmentLatestSnapshotQueryResponse> selectEquipmentListWithLatestSnapshots(@Param("request") EquipmentSearchRequest request);

    EquipmentDetailResponse selectEquipmentDetailById(@Param("equipmentId") Long equipmentId);

    Long selectEquipmentIdByCode(@Param("equipmentCode") String equipmentCode);

    Long selectEquipmentAgingParamIdByEquipmentId(@Param("equipmentId") Long equipmentId);

    Long selectEquipmentBaselineIdByEquipmentId(@Param("equipmentId") Long equipmentId);

    EquipmentAgingParamDetailResponse selectEquipmentAgingParamDetailById(@Param("equipmentAgingParamId") Long equipmentAgingParamId);

    EquipmentBaselineDetailResponse selectEquipmentBaselineDetailById(@Param("equipmentBaselineId") Long equipmentBaselineId);

    List<EquipmentAgingParamDetailResponse> selectEquipmentAgingParamHistory(@Param("request") EquipmentAgingParamSearchRequest request);

    EquipmentAgingParamDetailResponse selectLatestEquipmentAgingParamByEquipmentId(@Param("equipmentId") Long equipmentId);

    EquipmentAgingParamDetailResponse selectLatestEquipmentAgingParamBeforeOrAt(
        @Param("equipmentId") Long equipmentId,
        @Param("referenceTime") LocalDateTime referenceTime
    );

    EquipmentAgingParamDetailResponse selectFirstEquipmentAgingParamAfterOrAt(
        @Param("equipmentId") Long equipmentId,
        @Param("referenceTime") LocalDateTime referenceTime
    );

    List<EquipmentAgingParamDetailResponse> selectUncalculatedEquipmentAgingParamList();

    List<EquipmentBaselineDetailResponse> selectEquipmentBaselineHistory(@Param("request") EquipmentBaselineSearchRequest request);

    EquipmentBaselineDetailResponse selectLatestEquipmentBaselineByEquipmentId(@Param("equipmentId") Long equipmentId);

    EquipmentBaselineDetailResponse selectLatestEquipmentBaselineBeforeOrAt(
        @Param("equipmentId") Long equipmentId,
        @Param("referenceTime") LocalDateTime referenceTime
    );

    EquipmentBaselineDetailResponse selectFirstEquipmentBaselineAfterOrAt(
        @Param("equipmentId") Long equipmentId,
        @Param("referenceTime") LocalDateTime referenceTime
    );

    List<EquipmentBaselineDetailResponse> selectUncalculatedEquipmentBaselineList();
}
