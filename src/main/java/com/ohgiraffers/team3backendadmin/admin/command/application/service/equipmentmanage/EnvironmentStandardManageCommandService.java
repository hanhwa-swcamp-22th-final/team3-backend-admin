package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentStandardCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentStandardUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentStandardCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentStandardUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentStandard;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentStandardRepository;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EnvironmentStandardManageCommandService {

    private final EnvironmentStandardRepository environmentStandardRepository;
    private final IdGenerator idGenerator;

    /**
     * 환경 기준 생성 요청 정보를 기반으로 중복 코드를 확인한 뒤 새로운 환경 기준을 등록한다.
     * @param request 환경 기준 생성에 필요한 유형, 코드, 이름, 온도/습도 범위, 입자 기준 정보
     * @return 생성된 환경 기준의 식별자와 기본 정보
     */
    public EnvironmentStandardCreateResponse createEnvironmentStandard(EnvironmentStandardCreateRequest request) {
        if (environmentStandardRepository.findByEnvironmentCode(request.getEnvironmentCode()).isPresent()) {
            throw new BusinessException(ErrorCode.ENVIRONMENT_STANDARD_CODE_ALREADY_EXISTS);
        }

        EnvironmentStandard environmentStandard = EnvironmentStandard.builder()
            .environmentStandardId(idGenerator.generate())
            .environmentType(request.getEnvironmentType())
            .environmentCode(request.getEnvironmentCode())
            .environmentName(request.getEnvironmentName())
            .envTempMin(request.getEnvTempMin())
            .envTempMax(request.getEnvTempMax())
            .envHumidityMin(request.getEnvHumidityMin())
            .envHumidityMax(request.getEnvHumidityMax())
            .envParticleLimit(request.getEnvParticleLimit())
            .build();

        environmentStandardRepository.save(environmentStandard);

        return EnvironmentStandardCreateResponse.builder()
            .environmentStandardId(environmentStandard.getEnvironmentStandardId())
            .environmentType(environmentStandard.getEnvironmentType())
            .environmentCode(environmentStandard.getEnvironmentCode())
            .environmentName(environmentStandard.getEnvironmentName())
            .build();
    }

    /**
     * 기존 환경 기준을 조회하고 자기 자신을 제외한 코드 중복 여부를 확인한 뒤 요청 정보로 수정한다.
     * @param environmentStandardId 수정할 환경 기준 식별자
     * @param request 수정할 환경 기준 정보
     * @return 수정된 환경 기준의 식별자와 기본 정보
     */
    public EnvironmentStandardUpdateResponse updateEnvironmentStandard(Long environmentStandardId, EnvironmentStandardUpdateRequest request) {
        EnvironmentStandard environmentStandard = environmentStandardRepository.findById(environmentStandardId)
            .orElseThrow(() -> new BusinessException(ErrorCode.ENVIRONMENT_STANDARD_NOT_FOUND));

        environmentStandardRepository.findByEnvironmentCode(request.getEnvironmentCode())
            .filter(found -> !found.getEnvironmentStandardId().equals(environmentStandardId))
            .ifPresent(found -> {
                throw new BusinessException(ErrorCode.ENVIRONMENT_STANDARD_CODE_ALREADY_EXISTS);
            });

        environmentStandard.updateInfo(
            request.getEnvironmentType(),
            request.getEnvironmentCode(),
            request.getEnvironmentName(),
            request.getEnvTempMin(),
            request.getEnvTempMax(),
            request.getEnvHumidityMin(),
            request.getEnvHumidityMax(),
            request.getEnvParticleLimit()
        );

        return EnvironmentStandardUpdateResponse.builder()
            .environmentStandardId(environmentStandard.getEnvironmentStandardId())
            .environmentType(environmentStandard.getEnvironmentType())
            .environmentCode(environmentStandard.getEnvironmentCode())
            .environmentName(environmentStandard.getEnvironmentName())
            .build();
    }

    /**
     * 기존 환경 기준을 조회한 뒤 소프트 삭제 처리한다.
     * @param environmentStandardId 삭제할 환경 기준 식별자
     * @return 삭제 처리된 환경 기준의 식별자와 기본 정보
     */
    public EnvironmentStandardUpdateResponse deleteEnvironmentStandard(Long environmentStandardId) {
        EnvironmentStandard environmentStandard = environmentStandardRepository.findById(environmentStandardId)
            .orElseThrow(() -> new BusinessException(ErrorCode.ENVIRONMENT_STANDARD_NOT_FOUND));

        environmentStandard.softDelete();

        return EnvironmentStandardUpdateResponse.builder()
            .environmentStandardId(environmentStandard.getEnvironmentStandardId())
            .environmentType(environmentStandard.getEnvironmentType())
            .environmentCode(environmentStandard.getEnvironmentCode())
            .environmentName(environmentStandard.getEnvironmentName())
            .build();
    }
}
