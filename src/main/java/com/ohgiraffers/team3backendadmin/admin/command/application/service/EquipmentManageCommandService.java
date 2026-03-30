package com.ohgiraffers.team3backendadmin.admin.command.application.service;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentAgingParam;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EquipmentBaseline;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentStandardRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentAgingParamRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentBaselineRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentProcessRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
import com.ohgiraffers.team3backendadmin.admin.query.service.EquipmentQueryService;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentManageCommandService {

  private final EquipmentRepository equipmentRepository;
  private final EquipmentProcessRepository equipmentProcessRepository;
  private final EnvironmentStandardRepository environmentStandardRepository;
  private final EquipmentAgingParamRepository equipmentAgingParamRepository;
  private final EquipmentBaselineRepository equipmentBaselineRepository;
  private final EquipmentQueryService equipmentQueryService;
  private final IdGenerator idGenerator;

  public EquipmentCreateResponse createEquipment(EquipmentCreateRequest request) {

    if (equipmentQueryService.existsByEquipmentCode(request.getEquipmentCode())) {
      throw new IllegalArgumentException("Equipment code already exists.");
    }

    equipmentProcessRepository.findById(request.getEquipmentProcessId()).orElseThrow(
        () -> new IllegalArgumentException("Equipment process not found.")
    );
    environmentStandardRepository.findById(request.getEnvironmentStandardId()).orElseThrow(
        () -> new IllegalArgumentException("Environment standard not found.")
    );

    Long equipmentId = idGenerator.generate();
    Long equipmentAgingParamId = idGenerator.generate();
    Long equipmentBaselineId = idGenerator.generate();

    Equipment equipment = Equipment.builder()
        .equipmentId(equipmentId)
        .equipmentProcessId(request.getEquipmentProcessId())
        .environmentStandardId(request.getEnvironmentStandardId())
        .equipmentCode(request.getEquipmentCode())
        .equipmentName(request.getEquipmentName())
        .equipmentStatus(request.getEquipmentStatus())
        .equipmentGrade(request.getEquipmentGrade())
        .equipmentDescription(request.getEquipmentDescription())
        .build();

    EquipmentAgingParam equipmentAgingParam = EquipmentAgingParam.builder()
        .equipmentAgingParamId(equipmentAgingParamId)
        .equipmentId(equipmentId)
        .equipmentWarrantyMonth(request.getEquipmentWarrantyMonth())
        .equipmentDesignLifeMonths(request.getEquipmentDesignLifeMonths())
        .equipmentWearCoefficient(toBigDecimal(request.getEquipmentWearCoefficient()))
        .build();

    EquipmentBaseline equipmentBaseline = EquipmentBaseline.builder()
        .equipmentBaselineId(equipmentBaselineId)
        .equipmentId(equipmentId)
        .equipmentAgingParamId(equipmentAgingParamId)
        .build();

    equipmentRepository.save(equipment);
    equipmentAgingParamRepository.save(equipmentAgingParam);
    equipmentBaselineRepository.save(equipmentBaseline);

    return EquipmentCreateResponse.builder()
        .equipmentId(equipment.getEquipmentId())
        .equipmentCode(equipment.getEquipmentCode())
        .equipmentName(equipment.getEquipmentName())
        .equipmentStatus(equipment.getEquipmentStatus())
        .equipmentGrade(equipment.getEquipmentGrade())
        .build();
  }

  public void updateEquipment(Long equipmentId, EquipmentUpdateRequest request) {
    Equipment equipment = equipmentRepository.findById(equipmentId)
        .orElseThrow(() -> new IllegalArgumentException("Equipment not found."));

    equipmentProcessRepository.findById(request.getEquipmentProcessId()).orElseThrow(
        () -> new IllegalArgumentException("Equipment process not found.")
    );
    environmentStandardRepository.findById(request.getEnvironmentStandardId()).orElseThrow(
        () -> new IllegalArgumentException("Environment standard not found.")
    );

    Long equipmentAgingParamId = equipmentQueryService.getEquipmentAgingParamIdByEquipmentId(equipmentId);
    if (equipmentAgingParamId == null) {
      throw new IllegalArgumentException("Equipment aging parameter not found.");
    }

    EquipmentAgingParam equipmentAgingParam = equipmentAgingParamRepository.findById(equipmentAgingParamId)
        .orElseThrow(() -> new IllegalArgumentException("Equipment aging parameter not found."));

    equipment.update(
        request.getEquipmentProcessId(),
        request.getEnvironmentStandardId(),
        request.getEquipmentCode(),
        request.getEquipmentName(),
        request.getEquipmentStatus(),
        request.getEquipmentGrade(),
        request.getEquipmentDescription()
    );

    equipmentAgingParam.update(
        request.getEquipmentWarrantyMonth(),
        request.getEquipmentDesignLifeMonths(),
        toBigDecimal(request.getEquipmentWearCoefficient())
    );
  }

  public void deleteEquipment(Long equipmentId) {
    Equipment equipment = equipmentRepository.findById(equipmentId)
        .orElseThrow(() -> new IllegalArgumentException("Equipment not found."));

    Long equipmentBaselineId = equipmentQueryService.getEquipmentBaselineIdByEquipmentId(equipmentId);
    if (equipmentBaselineId != null) {
      equipmentBaselineRepository.deleteById(equipmentBaselineId);
    }

    Long equipmentAgingParamId = equipmentQueryService.getEquipmentAgingParamIdByEquipmentId(equipmentId);
    if (equipmentAgingParamId != null) {
      equipmentAgingParamRepository.deleteById(equipmentAgingParamId);
    }

    equipmentRepository.delete(equipment);
  }

  private BigDecimal toBigDecimal(Double value) {
    return value == null ? null : BigDecimal.valueOf(value);
  }
}