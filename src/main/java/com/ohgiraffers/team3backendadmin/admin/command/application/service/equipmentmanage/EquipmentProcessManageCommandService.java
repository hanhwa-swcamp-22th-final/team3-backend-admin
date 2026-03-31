package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentProcess;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentProcessRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.FactoryLineRepository;
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
   * 설비 공정 생성 요청을 검증한 뒤 새로운 공정 정보를 저장한다.
   * @param equipmentProcessCreateRequest 생성할 공정의 생산 라인, 코드, 이름 정보
   * @return 생성된 공정의 식별자와 기본 정보
   */
  public EquipmentProcessCreateResponse createEquipmentProcess(EquipmentProcessCreateRequest equipmentProcessCreateRequest) {
    factoryLineRepository.findById(equipmentProcessCreateRequest.getFactoryLineId()).orElseThrow(
        () -> new IllegalArgumentException("Factory line not found.")
    );

    if (equipmentProcessRepository.findByEquipmentProcessCode(equipmentProcessCreateRequest.getEquipmentProcessCode()).isPresent()) {
      throw new IllegalArgumentException("Equipment process code already exists");
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
   * 기존 설비 공정을 조회하고 요청 값으로 생산 라인, 코드, 이름을 수정한다.
   * @param equipmentProcessId 수정할 공정의 식별자
   * @param equipmentProcessUpdateRequest 수정할 공정의 생산 라인, 코드, 이름 정보
   * @return 수정된 공정의 식별자와 기본 정보
   */
  public EquipmentProcessUpdateResponse updateEquipmentProcess(Long equipmentProcessId,
                                                              EquipmentProcessUpdateRequest equipmentProcessUpdateRequest) {
    EquipmentProcess equipmentProcess = equipmentProcessRepository.findById(equipmentProcessId).orElseThrow(
        () -> new IllegalArgumentException("Equipment process not found.")
    );

    factoryLineRepository.findById(equipmentProcessUpdateRequest.getFactoryLineId()).orElseThrow(
        () -> new IllegalArgumentException("Factory line not found.")
    );

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
   * 기존 설비 공정을 조회한 뒤 소프트 삭제 처리한다.
   * @param equipmentProcessId 삭제할 공정의 식별자
   * @return 삭제 처리된 공정의 식별자와 기본 정보
   */
  public EquipmentProcessUpdateResponse deleteEquipmentProcess(Long equipmentProcessId) {
    EquipmentProcess equipmentProcess = equipmentProcessRepository.findById(equipmentProcessId).orElseThrow(
        () -> new IllegalArgumentException("Equipment process not found.")
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