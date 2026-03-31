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

  /**
   * 설비 코드 중복과 부모 데이터 존재 여부를 검증한 뒤 설비와 연관 기준 데이터를 함께 생성한다.
   * @param request 설비 기본 정보와 노후화 파라미터 값을 담은 생성 요청 값
   * @return 생성 완료된 설비 정보를 담은 응답 값
   */
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

  /**
   * 기존 설비와 연결된 노후화 파라미터를 함께 조회한 뒤 요청 값으로 수정한다.
   * @param equipmentId 수정 대상 설비의 식별자
   * @param request 수정할 설비 정보와 노후화 파라미터 값
   * @return 반환값 없음
   */
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

  /**
   * 메소드 의도
   * 설비를 삭제하면서 연결된 베이스라인과 노후화 파라미터도 함께 정리한다.
   * @param equipmentId 삭제 대상 설비의 식별자
   * @return 반환값 없음
   */
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

  /**
   * Double 타입의 마모 계수를 BigDecimal 타입으로 변환한다.
   * @param value 변환할 마모 계수 값
   * @return BigDecimal로 변환된 값, null 입력 시 null
   */
  private BigDecimal toBigDecimal(Double value) {
    return value == null ? null : BigDecimal.valueOf(value);
  }
}
