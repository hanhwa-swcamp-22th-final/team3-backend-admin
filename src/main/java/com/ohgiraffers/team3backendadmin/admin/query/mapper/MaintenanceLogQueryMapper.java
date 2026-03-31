package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.MaintenanceLogSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceLogDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceLogQueryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MaintenanceLogQueryMapper {
    List<MaintenanceLogQueryResponse> selectMaintenanceLogList(MaintenanceLogSearchRequest request);
    MaintenanceLogDetailResponse selectMaintenanceLogDetailById(Long maintenanceLogId);
}
