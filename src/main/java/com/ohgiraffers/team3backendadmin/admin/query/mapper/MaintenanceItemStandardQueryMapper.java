package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.MaintenanceItemStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.MaintenanceItemStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.MaintenanceItemStandardQueryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MaintenanceItemStandardQueryMapper {
    List<MaintenanceItemStandardQueryResponse> selectMaintenanceItemStandardList(MaintenanceItemStandardSearchRequest request);
    MaintenanceItemStandardDetailResponse selectMaintenanceItemStandardDetailById(Long maintenanceItemStandardId);
}
