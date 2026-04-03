package com.ohgiraffers.team3backendadmin.admin.command.application.service.domainkeyword;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.domainkeyword.DomainKeywordCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.domainkeyword.DomainKeywordUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.domainkeyword.DomainKeywordCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.domainkeyword.DomainKeywordDeleteResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.domainkeyword.DomainKeywordUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainKeyword;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DomainKeywordRepository;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DomainKeywordManageCommandService {

    private final DomainKeywordRepository domainKeywordRepository;
    private final IdGenerator idGenerator;

    /**
     * 도메인 키워드를 생성한다.
     * @param request 생성할 도메인 키워드 정보
     * @return 생성된 도메인 키워드 응답
     */
    public DomainKeywordCreateResponse createDomainKeyword(DomainKeywordCreateRequest request) {
        DomainKeyword domainKeyword = DomainKeyword.builder()
            .domainKeywordId(idGenerator.generate())
            .build();

        domainKeyword.update(
            request.getDomainKeyword(),
            request.getDomainKeywordDescription(),
            request.getDomainCompetencyCategory(),
            request.getDomainBaseScore(),
            request.getDomainWeight(),
            request.getDomainIsActive()
        );

        domainKeywordRepository.save(domainKeyword);

        return DomainKeywordCreateResponse.builder()
            .domainKeywordId(domainKeyword.getDomainKeywordId())
            .domainKeyword(domainKeyword.getDomainKeyword())
            .domainKeywordDescription(domainKeyword.getDomainKeywordDescription())
            .domainCompetencyCategory(domainKeyword.getDomainCompetencyCategory())
            .domainBaseScore(domainKeyword.getDomainBaseScore())
            .domainWeight(domainKeyword.getDomainWeight())
            .domainIsActive(domainKeyword.getDomainIsActive())
            .build();
    }

    /**
     * 도메인 키워드를 수정한다.
     * @param domainKeywordId 수정할 도메인 키워드 ID
     * @param request 수정할 도메인 키워드 정보
     * @return 수정된 도메인 키워드 응답
     */
    public DomainKeywordUpdateResponse updateDomainKeyword(Long domainKeywordId, DomainKeywordUpdateRequest request) {
        DomainKeyword domainKeyword = domainKeywordRepository.findById(domainKeywordId)
            .orElseThrow(() -> new BusinessException(ErrorCode.DOMAIN_KEYWORD_NOT_FOUND));

        domainKeyword.update(
            request.getDomainKeyword(),
            request.getDomainKeywordDescription(),
            request.getDomainCompetencyCategory(),
            request.getDomainBaseScore(),
            request.getDomainWeight(),
            request.getDomainIsActive()
        );

        return DomainKeywordUpdateResponse.builder()
            .domainKeywordId(domainKeyword.getDomainKeywordId())
            .domainKeyword(domainKeyword.getDomainKeyword())
            .domainKeywordDescription(domainKeyword.getDomainKeywordDescription())
            .domainCompetencyCategory(domainKeyword.getDomainCompetencyCategory())
            .domainBaseScore(domainKeyword.getDomainBaseScore())
            .domainWeight(domainKeyword.getDomainWeight())
            .domainIsActive(domainKeyword.getDomainIsActive())
            .build();
    }

    /**
     * 도메인 키워드를 삭제한다.
     * @param domainKeywordId 삭제할 도메인 키워드 ID
     * @return 삭제 결과 응답
     */
    public DomainKeywordDeleteResponse deleteDomainKeyword(Long domainKeywordId) {
        DomainKeyword domainKeyword = domainKeywordRepository.findById(domainKeywordId)
            .orElseThrow(() -> new BusinessException(ErrorCode.DOMAIN_KEYWORD_NOT_FOUND));

        domainKeywordRepository.delete(domainKeyword);

        return DomainKeywordDeleteResponse.builder()
            .domainKeywordId(domainKeywordId)
            .deleted(true)
            .build();
    }
}
