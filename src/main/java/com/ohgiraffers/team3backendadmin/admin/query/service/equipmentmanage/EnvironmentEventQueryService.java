package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EnvironmentEventSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentEventQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EnvironmentEventQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnvironmentEventQueryService {

    private final EnvironmentEventQueryMapper environmentEventQueryMapper;

    /**
     * 寃??議곌굔??留욌뒗 ?섍꼍 ?대깽??紐⑸줉??議고쉶?쒕떎.
     * @param request ?ㅻ퉬 ?앸퀎?먯? ?댄깉 ?좏삎???ы븿??議고쉶 議곌굔 ?뺣낫
     * @return 議고쉶 議곌굔??留욌뒗 ?섍꼍 ?대깽??紐⑸줉
     */
    public List<EnvironmentEventQueryResponse> getEnvironmentEventList(EnvironmentEventSearchRequest request) {
        return environmentEventQueryMapper.selectEnvironmentEventList(request);
    }

    /**
     * ?뱀젙 ?섍꼍 ?대깽?몄쓽 ?곸꽭 ?뺣낫瑜?議고쉶?쒕떎.
     * @param environmentEventId 議고쉶???섍꼍 ?대깽?몄쓽 ?앸퀎??     * @return 議고쉶???섍꼍 ?대깽?몄쓽 ?곸꽭 ?뺣낫
     */
    public EnvironmentEventDetailResponse getEnvironmentEventDetail(Long environmentEventId) {
        return environmentEventQueryMapper.selectEnvironmentEventDetailById(environmentEventId);
    }
}
