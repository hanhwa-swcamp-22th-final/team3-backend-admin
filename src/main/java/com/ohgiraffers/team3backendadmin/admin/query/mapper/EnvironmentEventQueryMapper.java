package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EnvironmentEventSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventQueryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EnvironmentEventQueryMapper {

    List<EnvironmentEventQueryResponse> selectEnvironmentEventList(@Param("request") EnvironmentEventSearchRequest request);

    EnvironmentEventDetailResponse selectEnvironmentEventDetailById(@Param("environmentEventId") Long environmentEventId);
}