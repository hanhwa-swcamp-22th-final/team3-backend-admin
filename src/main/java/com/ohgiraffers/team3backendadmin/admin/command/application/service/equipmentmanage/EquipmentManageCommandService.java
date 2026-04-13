package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.equipmentmanage.EquipmentCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.equipmentmanage.EquipmentUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.equipmentmanage.EquipmentCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.Equipment;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentAgingParam;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentBaseline;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.EquipmentGrade;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EnvironmentStandardRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentAgingParamRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentBaselineRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentProcessRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EquipmentRepository;
import com.ohgiraffers.team3backendadmin.common.exception.BusinessException;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipmentManageCommandService {

  private final EquipmentRepository equipmentRepository;
  private final EquipmentProcessRepository equipmentProcessRepository;
  private final EnvironmentStandardRepository environmentStandardRepository;
  private final EquipmentAgingParamRepository equipmentAgingParamRepository;
  private final EquipmentBaselineRepository equipmentBaselineRepository;
  private final IdGenerator idGenerator;
  private final EquipmentReferenceSnapshotCommandService equipmentReferenceSnapshotCommandService;

  /**
   * 설비 생성 요청을 검증한 뒤 설비, 노후 파라미터, baseline 정보를 함께 저장한다.
   * baseline에는 초기 보증 성능과 초기 오차율을 함께 기록한다.
   * @param request 설비 생성에 필요한 공정, 환경 기준, 코드, 이름, 노후도 및 baseline 초기 정보
   * @return 생성된 설비의 식별자와 기본 정보
   */
  public EquipmentCreateResponse createEquipment(EquipmentCreateRequest request) {

    if (equipmentRepository.findByEquipmentCode(request.getEquipmentCode()).isPresent()) {
      throw new BusinessException(ErrorCode.EQUIPMENT_CODE_ALREADY_EXISTS);
    }

    equipmentProcessRepository.findById(request.getEquipmentProcessId()).orElseThrow(
        () -> new BusinessException(ErrorCode.EQUIPMENT_PROCESS_NOT_FOUND)
    );
    environmentStandardRepository.findById(request.getEnvironmentStandardId()).orElseThrow(
        () -> new BusinessException(ErrorCode.ENVIRONMENT_STANDARD_NOT_FOUND)
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
        .equipmentInstallDate(toStartOfDay(request.getEquipmentInstallDate()))
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
        .equipmentStandardPerformanceRate(toBigDecimal(request.getEquipmentStandardPerformanceRate()))
        .equipmentBaselineErrorRate(toBigDecimal(request.getEquipmentBaselineErrorRate()))
        .equipmentIdx(initialEquipmentIdx(request.getEquipmentGrade()))
        .currentEquipmentGrade(request.getEquipmentGrade())
        .build();

    equipmentRepository.save(equipment);
    equipmentAgingParamRepository.save(equipmentAgingParam);
    equipmentBaselineRepository.save(equipmentBaseline);
    equipmentReferenceSnapshotCommandService.publishSnapshotAfterCommit(equipmentId);

    return EquipmentCreateResponse.builder()
        .equipmentId(equipment.getEquipmentId())
        .equipmentCode(equipment.getEquipmentCode())
        .equipmentName(equipment.getEquipmentName())
        .equipmentStatus(equipment.getEquipmentStatus())
        .equipmentGrade(equipment.getEquipmentGrade())
        .build();
  }

  /**
   * 기존 설비와 노후 파라미터를 조회한 뒤 요청 값으로 설비 정보를 수정한다.
   * 수정 시에는 동일한 설비를 제외하고 설비 코드 중복 여부를 확인한다.
   * @param equipmentId 수정할 설비의 식별자
   * @param request 수정할 설비의 공정, 환경 기준, 코드, 이름, 상태 정보
   * @return 반환값 없음
   */
  public void updateEquipment(Long equipmentId, EquipmentUpdateRequest request) {
    Equipment equipment = equipmentRepository.findById(equipmentId)
        .orElseThrow(() -> new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND));

    equipmentRepository.findByEquipmentCode(request.getEquipmentCode())
        .filter(found -> !found.getEquipmentId().equals(equipmentId))
        .ifPresent(found -> {
          throw new BusinessException(ErrorCode.EQUIPMENT_CODE_ALREADY_EXISTS);
        });

    equipmentProcessRepository.findById(request.getEquipmentProcessId()).orElseThrow(
        () -> new BusinessException(ErrorCode.EQUIPMENT_PROCESS_NOT_FOUND)
    );
    environmentStandardRepository.findById(request.getEnvironmentStandardId()).orElseThrow(
        () -> new BusinessException(ErrorCode.ENVIRONMENT_STANDARD_NOT_FOUND)
    );

    EquipmentAgingParam equipmentAgingParam = equipmentAgingParamRepository
        .findFirstByEquipmentIdOrderByEquipmentAgeCalculatedAtDescEquipmentAgingParamIdDesc(equipmentId)
        .orElseThrow(() -> new BusinessException(ErrorCode.EQUIPMENT_AGING_PARAM_NOT_FOUND));

    equipment.update(
        request.getEquipmentProcessId(),
        request.getEnvironmentStandardId(),
        request.getEquipmentCode(),
        request.getEquipmentName(),
        request.getEquipmentStatus(),
        request.getEquipmentGrade(),
        toStartOfDay(request.getEquipmentInstallDate()),
        request.getEquipmentDescription()
    );

    equipmentAgingParam.update(
        request.getEquipmentWarrantyMonth(),
        request.getEquipmentDesignLifeMonths(),
        toBigDecimal(request.getEquipmentWearCoefficient())
    );
    equipmentReferenceSnapshotCommandService.publishSnapshotAfterCommit(equipmentId);
  }

  /**
   * 기존 설비를 조회한 뒤 연관된 baseline, 노후 파라미터를 함께 삭제하고 설비를 제거한다.
   * @param equipmentId 삭제할 설비의 식별자
   * @return 반환값 없음
   */
  public void deleteEquipment(Long equipmentId) {
    Equipment equipment = equipmentRepository.findById(equipmentId)
        .orElseThrow(() -> new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND));

    equipmentReferenceSnapshotCommandService.publishDeletedSnapshotAfterCommit(
        equipment.getEquipmentId(),
        equipment.getEquipmentCode()
    );
    equipment.softDelete();
  }

  /**
   * Double 값을 BigDecimal 값으로 변환한다.
   * @param value 변환할 수치 값
   * @return BigDecimal로 변환한 값, null 입력 시 null
   */
  private LocalDateTime toStartOfDay(LocalDate value) {
    return value == null ? null : value.atStartOfDay();
  }
  private BigDecimal initialEquipmentIdx(EquipmentGrade grade) {
    if (grade == null) {
      return null;
    }

    return switch (grade) {
      case S -> BigDecimal.valueOf(1.00);
      case A -> BigDecimal.valueOf(0.90);
      case B -> BigDecimal.valueOf(0.80);
      case C -> BigDecimal.valueOf(0.70);
    };
  }

  private BigDecimal toBigDecimal(Double value) {
    return value == null ? null : BigDecimal.valueOf(value);
  }
}
