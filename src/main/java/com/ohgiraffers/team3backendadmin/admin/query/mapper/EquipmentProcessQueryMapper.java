package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EquipmentProcessSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentProcessDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EquipmentProcessQueryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EquipmentProcessQueryMapper {

    List<EquipmentProcessQueryResponse> selectEquipmentProcessList(@Param("request") EquipmentProcessSearchRequest request);

    EquipmentProcessDetailResponse selectEquipmentProcessDetailById(@Param("equipmentProcessId") Long equipmentProcessId);

    Long selectEquipmentProcessIdByCode(@Param("equipmentProcessCode") String equipmentProcessCode);
}
