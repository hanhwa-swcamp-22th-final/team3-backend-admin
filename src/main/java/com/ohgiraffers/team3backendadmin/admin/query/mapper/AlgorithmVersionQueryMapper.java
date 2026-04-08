package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.algorithmversion.AlgorithmVersionSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.algorithmversion.AlgorithmVersionDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.algorithmversion.AlgorithmVersionQueryResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlgorithmVersionQueryMapper {

    List<AlgorithmVersionQueryResponse> selectAlgorithmVersionList(AlgorithmVersionSearchRequest request);

    AlgorithmVersionDetailResponse selectAlgorithmVersionDetailById(Long algorithmVersionId);
}