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
   * ?ㅻ퉬 肄붾뱶 以묐났怨?遺紐??곗씠??議댁옱 ?щ?瑜?寃利앺븳 ???ㅻ퉬? 愿??湲곗? ?곗씠?곕? ?④퍡 ?앹꽦?쒕떎.
   * @param request ?ㅻ퉬 湲곕낯 ?뺣낫? ?명썑???뚮씪誘명꽣 媛믪쓣 ?댁? ?앹꽦 ?붿껌 ?뺣낫
   * @return ?앹꽦???꾨즺???ㅻ퉬???묐떟 ?뺣낫
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
   * 湲곗〈 ?ㅻ퉬? ?곌껐???명썑???뚮씪誘명꽣瑜?議고쉶?????붿껌 媛믪쑝濡??섏젙?쒕떎.
   * @param equipmentId ?섏젙???ㅻ퉬???앸퀎??
   * @param request ?섏젙???ㅻ퉬 ?뺣낫? ?명썑???뚮씪誘명꽣 媛?
   * @return 諛섑솚媛??놁쓬
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
   * ?ㅻ퉬瑜???젣?섎㈃???곌껐??踰좎씠?ㅻ씪?멸낵 ?명썑???뚮씪誘명꽣???④퍡 ?뺣━?쒕떎.
   * @param equipmentId ??젣???ㅻ퉬???앸퀎??
   * @return 諛섑솚媛??놁쓬
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
   * Double ??낆쓽 留덈え 怨꾩닔瑜?BigDecimal ??낆쑝濡?蹂?섑븳??
   * @param value 蹂?섑븷 留덈え 怨꾩닔 媛?
   * @return BigDecimal濡?蹂?섎맂 媛? null ?낅젰 ??null
   */
  private BigDecimal toBigDecimal(Double value) {
    return value == null ? null : BigDecimal.valueOf(value);
  }
}