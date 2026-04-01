package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentProcessRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.FactoryLineRepository;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentProcessManageCommandService {

  private final EquipmentProcessRepository equipmentProcessRepository;
  private final FactoryLineRepository factoryLineRepository;
  private final IdGenerator idGenerator;

  /**
   * 생산 라인 존재 여부와 중복 코드를 확인한 뒤 새로운 공정을 등록한다.
   * @param equipmentProcessCreateRequest 생성할 공정의 생산 라인, 코드, 이름 정보
   * @return 생성된 공정의 식별자와 기본 정보
   */
  public EquipmentProcessCreateResponse createEquipmentProcess(EquipmentProcessCreateRequest equipmentProcessCreateRequest) {
    factoryLineRepository.findById(equipmentProcessCreateRequest.getFactoryLineId()).orElseThrow(
        () -> new BusinessException(ErrorCode.FACTORY_LINE_NOT_FOUND)
    );

    if (equipmentProcessRepository.findByEquipmentProcessCode(equipmentProcessCreateRequest.getEquipmentProcessCode()).isPresent()) {
      throw new BusinessException(ErrorCode.EQUIPMENT_PROCESS_CODE_ALREADY_EXISTS);
    }

    EquipmentProcess equipmentProcess = EquipmentProcess.builder()
        .equipmentProcessId(idGenerator.generate())
        .factoryLineId(equipmentProcessCreateRequest.getFactoryLineId())
        .equipmentProcessCode(equipmentProcessCreateRequest.getEquipmentProcessCode())
        .equipmentProcessName(equipmentProcessCreateRequest.getEquipmentProcessName())
        .build();

    equipmentProcessRepository.save(equipmentProcess);

    return EquipmentProcessCreateResponse.builder()
        .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
        .factoryLineId(equipmentProcess.getFactoryLineId())
        .equipmentProcessCode(equipmentProcess.getEquipmentProcessCode())
        .equipmentProcessName(equipmentProcess.getEquipmentProcessName())
        .build();
  }

  /**
   * 기존 공정과 생산 라인을 조회하고 자기 자신을 제외한 코드 중복 여부를 확인한 뒤 요청 정보로 수정한다.
   * @param equipmentProcessId 수정할 공정 식별자
   * @param equipmentProcessUpdateRequest 수정할 공정의 생산 라인, 코드, 이름 정보
   * @return 수정된 공정의 식별자와 기본 정보
   */
  public EquipmentProcessUpdateResponse updateEquipmentProcess(Long equipmentProcessId,
                                                              EquipmentProcessUpdateRequest equipmentProcessUpdateRequest) {
    EquipmentProcess equipmentProcess = equipmentProcessRepository.findById(equipmentProcessId).orElseThrow(
        () -> new BusinessException(ErrorCode.EQUIPMENT_PROCESS_NOT_FOUND)
    );

    factoryLineRepository.findById(equipmentProcessUpdateRequest.getFactoryLineId()).orElseThrow(
        () -> new BusinessException(ErrorCode.FACTORY_LINE_NOT_FOUND)
    );

    equipmentProcessRepository.findByEquipmentProcessCode(equipmentProcessUpdateRequest.getEquipmentProcessCode())
        .filter(found -> !found.getEquipmentProcessId().equals(equipmentProcessId))
        .ifPresent(found -> {
          throw new BusinessException(ErrorCode.EQUIPMENT_PROCESS_CODE_ALREADY_EXISTS);
        });

    equipmentProcess.updateInfo(
        equipmentProcessUpdateRequest.getFactoryLineId(),
        equipmentProcessUpdateRequest.getEquipmentProcessCode(),
        equipmentProcessUpdateRequest.getEquipmentProcessName()
    );

    return EquipmentProcessUpdateResponse.builder()
        .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
        .factoryLineId(equipmentProcess.getFactoryLineId())
        .equipmentProcessCode(equipmentProcess.getEquipmentProcessCode())
        .equipmentProcessName(equipmentProcess.getEquipmentProcessName())
        .build();
  }

  /**
   * 기존 공정을 조회한 뒤 소프트 삭제 처리한다.
   * @param equipmentProcessId 삭제할 공정 식별자
   * @return 삭제 처리된 공정의 식별자와 기본 정보
   */
  public EquipmentProcessUpdateResponse deleteEquipmentProcess(Long equipmentProcessId) {
    EquipmentProcess equipmentProcess = equipmentProcessRepository.findById(equipmentProcessId).orElseThrow(
        () -> new BusinessException(ErrorCode.EQUIPMENT_PROCESS_NOT_FOUND)
    );

    equipmentProcess.softDelete();

    return EquipmentProcessUpdateResponse.builder()
        .equipmentProcessId(equipmentProcess.getEquipmentProcessId())
        .factoryLineId(equipmentProcess.getFactoryLineId())
        .equipmentProcessCode(equipmentProcess.getEquipmentProcessCode())
        .equipmentProcessName(equipmentProcess.getEquipmentProcessName())
        .build();
  }
}
