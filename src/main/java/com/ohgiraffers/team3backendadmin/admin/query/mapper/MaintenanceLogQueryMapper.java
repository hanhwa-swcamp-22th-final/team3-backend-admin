package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.MaintenanceLogSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceLogDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceLogQueryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MaintenanceLogQueryMapper {
    List<MaintenanceLogQueryResponse> selectMaintenanceLogList(@Param("request") MaintenanceLogSearchRequest request);
    MaintenanceLogDetailResponse selectMaintenanceLogDetailById(@Param("maintenanceLogId") Long maintenanceLogId);
    MaintenanceLogDetailResponse selectLatestMaintenanceLogByEquipmentId(@Param("equipmentId") Long equipmentId);
    MaintenanceLogDetailResponse selectLatestMaintenanceLogBeforeOrAt(
        @Param("equipmentId") Long equipmentId,
        @Param("referenceDate") LocalDate referenceDate
    );
    MaintenanceLogDetailResponse selectFirstMaintenanceLogAfterOrAt(
        @Param("equipmentId") Long equipmentId,
        @Param("referenceDate") LocalDate referenceDate
    );
    List<MaintenanceLogQueryResponse> selectAbnormalOrIncompleteMaintenanceLogList();
}
