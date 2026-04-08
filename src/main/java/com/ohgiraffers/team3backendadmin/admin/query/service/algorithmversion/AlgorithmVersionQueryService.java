package com.ohgiraffers.team3backendadmin.admin.query.service.algorithmversion;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.algorithmversion.AlgorithmVersionSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.algorithmversion.AlgorithmVersionDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.algorithmversion.AlgorithmVersionQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.AlgorithmVersionQueryMapper;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlgorithmVersionQueryService {

    private final AlgorithmVersionQueryMapper algorithmVersionQueryMapper;

    public List<AlgorithmVersionQueryResponse> getAlgorithmVersionList(AlgorithmVersionSearchRequest request) {
        return algorithmVersionQueryMapper.selectAlgorithmVersionList(request);
    }

    public AlgorithmVersionDetailResponse getAlgorithmVersionDetail(Long algorithmVersionId) {
        AlgorithmVersionDetailResponse response = algorithmVersionQueryMapper
            .selectAlgorithmVersionDetailById(algorithmVersionId);
        if (response == null) {
            throw new BusinessException(ErrorCode.ALGORITHM_VERSION_NOT_FOUND);
        }
        return response;
    }
}