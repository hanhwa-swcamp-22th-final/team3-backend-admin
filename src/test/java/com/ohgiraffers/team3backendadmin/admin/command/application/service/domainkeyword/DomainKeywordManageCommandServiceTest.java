package com.ohgiraffers.team3backendadmin.admin.command.application.service.domainkeyword;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DomainKeywordCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.DomainKeywordUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.DomainKeywordCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.DomainKeywordDeleteResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.DomainKeywordUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainCompetencyCategory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainKeyword;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DomainKeywordRepository;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DomainKeywordManageCommandServiceTest {

    @Mock
    private DomainKeywordRepository domainKeywordRepository;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private DomainKeywordManageCommandService domainKeywordManageCommandService;

    private DomainKeyword domainKeyword;
    private DomainKeywordCreateRequest createRequest;
    private DomainKeywordUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        domainKeyword = DomainKeyword.builder()
            .domainKeywordId(9001L)
            .domainKeyword("안전 준수")
            .domainKeywordDescription("안전 수칙을 지키는 역량")
            .domainCompetencyCategory(DomainCompetencyCategory.SAFETY)
            .domainBaseScore(BigDecimal.valueOf(3))
            .domainWeight(BigDecimal.valueOf(2))
            .domainIsActive(true)
            .build();

        createRequest = DomainKeywordCreateRequest.builder()
            .domainKeyword("안전 준수")
            .domainKeywordDescription("안전 수칙을 지키는 역량")
            .domainCompetencyCategory(DomainCompetencyCategory.SAFETY)
            .domainBaseScore(BigDecimal.valueOf(3))
            .domainWeight(BigDecimal.valueOf(2))
            .domainIsActive(true)
            .build();

        updateRequest = DomainKeywordUpdateRequest.builder()
            .domainKeyword("기술 혁신")
            .domainKeywordDescription("기술 혁신을 주도하는 역량")
            .domainCompetencyCategory(DomainCompetencyCategory.INNOVATION)
            .domainBaseScore(BigDecimal.valueOf(5))
            .domainWeight(BigDecimal.valueOf(3))
            .domainIsActive(false)
            .build();
    }

    @Test
    @DisplayName("Create domain keyword success")
    void createDomainKeyword_success() {
        when(idGenerator.generate()).thenReturn(9001L);
        when(domainKeywordRepository.save(any(DomainKeyword.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DomainKeywordCreateResponse response = domainKeywordManageCommandService.createDomainKeyword(createRequest);

        ArgumentCaptor<DomainKeyword> captor = ArgumentCaptor.forClass(DomainKeyword.class);
        verify(domainKeywordRepository).save(captor.capture());

        DomainKeyword savedDomainKeyword = captor.getValue();
        assertEquals(9001L, savedDomainKeyword.getDomainKeywordId());
        assertEquals("안전 준수", savedDomainKeyword.getDomainKeyword());
        assertEquals("안전 수칙을 지키는 역량", savedDomainKeyword.getDomainKeywordDescription());
        assertEquals(DomainCompetencyCategory.SAFETY, savedDomainKeyword.getDomainCompetencyCategory());
        assertEquals(9001L, response.getDomainKeywordId());
    }

    @Test
    @DisplayName("Update domain keyword success")
    void updateDomainKeyword_success() {
        when(domainKeywordRepository.findById(9001L)).thenReturn(Optional.of(domainKeyword));

        DomainKeywordUpdateResponse response = domainKeywordManageCommandService.updateDomainKeyword(9001L, updateRequest);

        assertEquals("기술 혁신", domainKeyword.getDomainKeyword());
        assertEquals("기술 혁신을 주도하는 역량", domainKeyword.getDomainKeywordDescription());
        assertEquals(DomainCompetencyCategory.INNOVATION, domainKeyword.getDomainCompetencyCategory());
        assertEquals(false, domainKeyword.getDomainIsActive());
        assertEquals(9001L, response.getDomainKeywordId());
    }

    @Test
    @DisplayName("Update domain keyword failure when not found")
    void updateDomainKeyword_whenNotFound_thenThrow() {
        when(domainKeywordRepository.findById(9999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
            () -> domainKeywordManageCommandService.updateDomainKeyword(9999L, updateRequest));

        assertEquals(ErrorCode.DOMAIN_KEYWORD_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Delete domain keyword success")
    void deleteDomainKeyword_success() {
        when(domainKeywordRepository.findById(9001L)).thenReturn(Optional.of(domainKeyword));

        DomainKeywordDeleteResponse response = domainKeywordManageCommandService.deleteDomainKeyword(9001L);

        verify(domainKeywordRepository).delete(domainKeyword);
        assertEquals(9001L, response.getDomainKeywordId());
        assertEquals(true, response.getDeleted());
    }

    @Test
    @DisplayName("Delete domain keyword failure when not found")
    void deleteDomainKeyword_whenNotFound_thenThrow() {
        when(domainKeywordRepository.findById(9999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
            () -> domainKeywordManageCommandService.deleteDomainKeyword(9999L));

        assertEquals(ErrorCode.DOMAIN_KEYWORD_NOT_FOUND, exception.getErrorCode());
    }
}
