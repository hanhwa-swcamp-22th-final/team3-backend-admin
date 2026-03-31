package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.MaintenanceItemStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceItemStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.MaintenanceItemStandardQueryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MaintenanceItemStandardQueryMapper {
    List<MaintenanceItemStandardQueryResponse> selectMaintenanceItemStandardList(MaintenanceItemStandardSearchRequest request);
    MaintenanceItemStandardDetailResponse selectMaintenanceItemStandardDetailById(Long maintenanceItemStandardId);
}
