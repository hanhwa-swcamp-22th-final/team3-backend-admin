package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.equipmentmanage.EnvironmentEventSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentEventDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.equipmentmanage.EnvironmentEventQueryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EnvironmentEventQueryMapper {

    List<EnvironmentEventQueryResponse> selectEnvironmentEventList(@Param("request") EnvironmentEventSearchRequest request);

    EnvironmentEventDetailResponse selectEnvironmentEventDetailById(@Param("environmentEventId") Long environmentEventId);

    EnvironmentEventDetailResponse selectLatestEnvironmentEventByEquipmentId(@Param("equipmentId") Long equipmentId);

    EnvironmentEventDetailResponse selectLatestEnvironmentEventBeforeOrAt(
        @Param("equipmentId") Long equipmentId,
        @Param("referenceTime") java.time.LocalDateTime referenceTime
    );

    EnvironmentEventDetailResponse selectFirstEnvironmentEventAfterOrAt(
        @Param("equipmentId") Long equipmentId,
        @Param("referenceTime") java.time.LocalDateTime referenceTime
    );

    List<EnvironmentEventQueryResponse> selectUnresolvedEnvironmentEventList();
}
