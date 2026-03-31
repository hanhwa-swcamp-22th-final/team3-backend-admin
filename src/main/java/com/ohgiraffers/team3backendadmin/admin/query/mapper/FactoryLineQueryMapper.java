package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.FactoryLineSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.FactoryLineQueryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FactoryLineQueryMapper {

    List<FactoryLineQueryResponse> selectFactoryLineList(@Param("request") FactoryLineSearchRequest request);

    FactoryLineDetailResponse selectFactoryLineDetailById(@Param("factoryLineId") Long factoryLineId);

    Long selectFactoryLineIdByCode(@Param("factoryLineCode") String factoryLineCode);
}
