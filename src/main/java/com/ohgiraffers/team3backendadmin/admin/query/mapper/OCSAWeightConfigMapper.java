package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.OCSAWeightConfigResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OCSAWeightConfigMapper {

    List<OCSAWeightConfigResponse> findAll();
}
