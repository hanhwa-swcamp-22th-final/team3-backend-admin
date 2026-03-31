package com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.query.dto.request.EnvironmentStandardSearchRequest;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentStandardDetailResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.EnvironmentStandardQueryResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.EnvironmentStandardQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnvironmentStandardQueryService {

    private final EnvironmentStandardQueryMapper environmentStandardQueryMapper;

    /**
     * 寃??議곌굔??留욌뒗 ?섍꼍 湲곗? 紐⑸줉??議고쉶?쒕떎.
     * @param request ?ㅼ썙?쒖? ?섍꼍 ?좏삎???ы븿??議고쉶 議곌굔 ?뺣낫
     * @return 議고쉶 議곌굔??留욌뒗 ?섍꼍 湲곗? 紐⑸줉
     */
    public List<EnvironmentStandardQueryResponse> getEnvironmentStandardList(EnvironmentStandardSearchRequest request) {
        return environmentStandardQueryMapper.selectEnvironmentStandardList(request);
    }

    /**
     * ?뱀젙 ?섍꼍 湲곗????곸꽭 ?뺣낫瑜?議고쉶?쒕떎.
     * @param environmentStandardId 議고쉶???섍꼍 湲곗????앸퀎??     * @return 議고쉶???섍꼍 湲곗????곸꽭 ?뺣낫
     */
    public EnvironmentStandardDetailResponse getEnvironmentStandardDetail(Long environmentStandardId) {
        return environmentStandardQueryMapper.selectEnvironmentStandardDetailById(environmentStandardId);
    }

    /**
     * ?숈씪???섍꼍 湲곗? 肄붾뱶媛 ?대? ?ъ슜 以묒씤吏 ?뺤씤?쒕떎.
     * @param environmentCode 以묐났 ?щ?瑜??뺤씤???섍꼍 湲곗? 肄붾뱶
     * @return ?ъ슜 以묒씠硫?true, ?꾨땲硫?false
     */
    public boolean existsByEnvironmentCode(String environmentCode) {
        return environmentStandardQueryMapper.selectEnvironmentStandardIdByCode(environmentCode) != null;
    }
}
