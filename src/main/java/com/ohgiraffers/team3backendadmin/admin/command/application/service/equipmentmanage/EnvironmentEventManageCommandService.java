package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentEventCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EnvironmentEventUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentEventCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EnvironmentEventUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.environment.EnvironmentEvent;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentEventRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EnvironmentEventManageCommandService {

    private final EnvironmentEventRepository environmentEventRepository;
    private final EquipmentRepository equipmentRepository;
    private final IdGenerator idGenerator;

    /**
     * 설비 존재 여부를 확인한 뒤 환경 이벤트 측정 정보를 저장한다.
     * @param request 환경 이벤트 생성에 필요한 설비 식별자와 측정 결과 정보
     * @return 생성된 환경 이벤트의 식별자와 기본 정보
     */
    public EnvironmentEventCreateResponse createEnvironmentEvent(EnvironmentEventCreateRequest request) {
        equipmentRepository.findById(request.getEquipmentId())
            .orElseThrow(() -> new IllegalArgumentException("Equipment not found."));

        EnvironmentEvent environmentEvent = EnvironmentEvent.builder()
            .environmentEventId(idGenerator.generate())
            .equipmentId(request.getEquipmentId())
            .envTemperature(request.getEnvTemperature())
            .envHumidity(request.getEnvHumidity())
            .envParticleCnt(request.getEnvParticleCnt())
            .envDeviationType(request.getEnvDeviationType())
            .envCorrectionApplied(request.getEnvCorrectionApplied())
            .envDetectedAt(request.getEnvDetectedAt())
            .build();

        environmentEventRepository.save(environmentEvent);

        return EnvironmentEventCreateResponse.builder()
            .environmentEventId(environmentEvent.getEnvironmentEventId())
            .equipmentId(environmentEvent.getEquipmentId())
            .envDeviationType(environmentEvent.getEnvDeviationType())
            .envCorrectionApplied(environmentEvent.getEnvCorrectionApplied())
            .build();
    }

    /**
     * 기존 환경 이벤트와 설비를 확인한 뒤 요청 값으로 측정 정보를 수정한다.
     * @param environmentEventId 수정할 환경 이벤트의 식별자
     * @param request 수정할 환경 이벤트 정보
     * @return 수정된 환경 이벤트의 식별자와 기본 정보
     */
    public EnvironmentEventUpdateResponse updateEnvironmentEvent(Long environmentEventId, EnvironmentEventUpdateRequest request) {
        EnvironmentEvent environmentEvent = environmentEventRepository.findById(environmentEventId)
            .orElseThrow(() -> new IllegalArgumentException("Environment event not found."));

        equipmentRepository.findById(request.getEquipmentId())
            .orElseThrow(() -> new IllegalArgumentException("Equipment not found."));

        environmentEvent.updateInfo(
            request.getEquipmentId(),
            request.getEnvTemperature(),
            request.getEnvHumidity(),
            request.getEnvParticleCnt(),
            request.getEnvDeviationType(),
            request.getEnvCorrectionApplied(),
            request.getEnvDetectedAt()
        );

        return EnvironmentEventUpdateResponse.builder()
            .environmentEventId(environmentEvent.getEnvironmentEventId())
            .equipmentId(environmentEvent.getEquipmentId())
            .envDeviationType(environmentEvent.getEnvDeviationType())
            .envCorrectionApplied(environmentEvent.getEnvCorrectionApplied())
            .build();
    }

    /**
     * 기존 환경 이벤트를 조회한 뒤 삭제 처리한다.
     * @param environmentEventId 삭제할 환경 이벤트의 식별자
     * @return 삭제 처리된 환경 이벤트의 식별자와 기본 정보
     */
    public EnvironmentEventUpdateResponse deleteEnvironmentEvent(Long environmentEventId) {
        EnvironmentEvent environmentEvent = environmentEventRepository.findById(environmentEventId)
            .orElseThrow(() -> new IllegalArgumentException("Environment event not found."));

        environmentEventRepository.delete(environmentEvent);

        return EnvironmentEventUpdateResponse.builder()
            .environmentEventId(environmentEvent.getEnvironmentEventId())
            .equipmentId(environmentEvent.getEquipmentId())
            .envDeviationType(environmentEvent.getEnvDeviationType())
            .envCorrectionApplied(environmentEvent.getEnvCorrectionApplied())
            .build();
    }
}
