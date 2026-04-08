package com.ohgiraffers.team3backendadmin.admin.command.application.service.algorithmversion;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.algorithmversion.AlgorithmVersionCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.algorithmversion.AlgorithmVersionUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.algorithmversion.AlgorithmVersionCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.algorithmversion.AlgorithmVersionDeleteResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.algorithmversion.AlgorithmVersionUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.algorithmversion.AlgorithmVersion;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.AlgorithmVersionRepository;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlgorithmVersionManageCommandService {

    private final AlgorithmVersionRepository algorithmVersionRepository;
    private final IdGenerator idGenerator;

    public AlgorithmVersionCreateResponse createAlgorithmVersion(AlgorithmVersionCreateRequest request) {
        validateDuplicateVersionNo(request.getVersionNo(), null);
        validateDuplicateImplementationKey(request.getImplementationKey(), null);

        AlgorithmVersion algorithmVersion = AlgorithmVersion.builder()
            .algorithmVersionId(idGenerator.generate())
            .build();

        algorithmVersion.update(
            request.getVersionNo(),
            request.getImplementationKey(),
            request.getDescription(),
            request.getIsActive()
        );

        algorithmVersionRepository.save(algorithmVersion);

        return AlgorithmVersionCreateResponse.builder()
            .algorithmVersionId(algorithmVersion.getAlgorithmVersionId())
            .versionNo(algorithmVersion.getVersionNo())
            .implementationKey(algorithmVersion.getImplementationKey())
            .description(algorithmVersion.getDescription())
            .isActive(algorithmVersion.getIsActive())
            .build();
    }

    public AlgorithmVersionUpdateResponse updateAlgorithmVersion(
        Long algorithmVersionId,
        AlgorithmVersionUpdateRequest request
    ) {
        AlgorithmVersion algorithmVersion = algorithmVersionRepository.findById(algorithmVersionId)
            .orElseThrow(() -> new BusinessException(ErrorCode.ALGORITHM_VERSION_NOT_FOUND));

        validateDuplicateVersionNo(request.getVersionNo(), algorithmVersionId);
        validateDuplicateImplementationKey(request.getImplementationKey(), algorithmVersionId);

        algorithmVersion.update(
            request.getVersionNo(),
            request.getImplementationKey(),
            request.getDescription(),
            request.getIsActive()
        );

        return AlgorithmVersionUpdateResponse.builder()
            .algorithmVersionId(algorithmVersion.getAlgorithmVersionId())
            .versionNo(algorithmVersion.getVersionNo())
            .implementationKey(algorithmVersion.getImplementationKey())
            .description(algorithmVersion.getDescription())
            .isActive(algorithmVersion.getIsActive())
            .build();
    }

    public AlgorithmVersionDeleteResponse deleteAlgorithmVersion(Long algorithmVersionId) {
        AlgorithmVersion algorithmVersion = algorithmVersionRepository.findById(algorithmVersionId)
            .orElseThrow(() -> new BusinessException(ErrorCode.ALGORITHM_VERSION_NOT_FOUND));

        algorithmVersionRepository.delete(algorithmVersion);

        return AlgorithmVersionDeleteResponse.builder()
            .algorithmVersionId(algorithmVersionId)
            .deleted(true)
            .build();
    }

    private void validateDuplicateVersionNo(String versionNo, Long algorithmVersionId) {
        boolean duplicated = algorithmVersionId == null
            ? algorithmVersionRepository.existsByVersionNo(versionNo)
            : algorithmVersionRepository.existsByVersionNoAndAlgorithmVersionIdNot(versionNo, algorithmVersionId);

        if (duplicated) {
            throw new BusinessException(ErrorCode.DUPLICATE_FIELD, "이미 사용 중인 버전 번호입니다.");
        }
    }

    private void validateDuplicateImplementationKey(String implementationKey, Long algorithmVersionId) {
        boolean duplicated = algorithmVersionId == null
            ? algorithmVersionRepository.existsByImplementationKey(implementationKey)
            : algorithmVersionRepository.existsByImplementationKeyAndAlgorithmVersionIdNot(
                implementationKey,
                algorithmVersionId
            );

        if (duplicated) {
            throw new BusinessException(ErrorCode.DUPLICATE_FIELD, "이미 사용 중인 구현 키입니다.");
        }
    }
}