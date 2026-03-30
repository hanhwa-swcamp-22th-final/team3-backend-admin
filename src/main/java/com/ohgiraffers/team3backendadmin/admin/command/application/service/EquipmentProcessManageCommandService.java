package com.ohgiraffers.team3backendadmin.admin.command.application.service;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentProcessUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentProcessUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentProcess;
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
