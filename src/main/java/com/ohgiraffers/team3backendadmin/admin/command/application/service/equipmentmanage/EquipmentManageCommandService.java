package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EquipmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.EquipmentCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentAgingParam;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentBaseline;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentStandardRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentAgingParamRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentBaselineRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentProcessRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
import com.ohgiraffers.team3backendadmin.admin.query.service.equipmentmanage.EquipmentQueryService;
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
   * 설비 생성 요청을 검증한 뒤 설비, 노후화 파라미터, 기준 정보를 함께 저장한다.
   * @param request 설비 생성에 필요한 공정, 환경 기준, 코드, 이름, 상태 정보
   * @return 생성된 설비의 식별자와 기본 정보
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
   * 기존 설비와 노후화 파라미터를 조회한 뒤 요청 값으로 설비 정보를 수정한다.
   * @param equipmentId 수정할 설비의 식별자
   * @param request 수정할 설비의 공정, 환경 기준, 코드, 이름, 상태 정보
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
   * 기존 설비를 조회한 뒤 관련 기준 데이터와 함께 설비를 삭제한다.
   * @param equipmentId 삭제할 설비의 식별자
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
   * Double 값을 BigDecimal 값으로 변환한다.
   * @param value 변환할 마모 계수 값
   * @return BigDecimal로 변환된 값, null 입력 시 null
   */
  private BigDecimal toBigDecimal(Double value) {
    return value == null ? null : BigDecimal.valueOf(value);
  }
}