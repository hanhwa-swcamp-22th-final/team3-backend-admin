package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EnvironmentStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentStandardQueryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EnvironmentStandardQueryMapper {

    List<EnvironmentStandardQueryResponse> selectEnvironmentStandardList(@Param("request") EnvironmentStandardSearchRequest request);

    EnvironmentStandardDetailResponse selectEnvironmentStandardDetailById(@Param("environmentStandardId") Long environmentStandardId);

    Long selectEnvironmentStandardIdByCode(@Param("environmentCode") String environmentCode);
}