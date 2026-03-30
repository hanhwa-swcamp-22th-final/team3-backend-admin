package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EquipmentSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EquipmentQueryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EquipmentQueryMapper {

    List<EquipmentQueryResponse> selectEquipmentList(@Param("request") EquipmentSearchRequest request);

    EquipmentDetailResponse selectEquipmentDetailById(@Param("equipmentId") Long equipmentId);

    Long selectEquipmentIdByCode(@Param("equipmentCode") String equipmentCode);

    Long selectEquipmentAgingParamIdByEquipmentId(@Param("equipmentId") Long equipmentId);

    Long selectEquipmentBaselineIdByEquipmentId(@Param("equipmentId") Long equipmentId);
}
