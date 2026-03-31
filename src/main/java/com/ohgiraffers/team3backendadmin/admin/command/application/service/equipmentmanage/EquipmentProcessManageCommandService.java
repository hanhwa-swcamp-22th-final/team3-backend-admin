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
   * ?遊붋嶺???諛댄뀰 ??源녿데 ?브퀡???????? ??ㅻ쾴???袁⑤?獄?繞벿살탮????????筌먦끉逾??????ル맪????ㅻ쾴?????諛댁뎽??類ｋ펲.
   * @param equipmentProcessCreateRequest ??諛댁뎽????ㅻ쾴?????諛댄뀰 ??源녿데, ?袁⑤?獄? ???藥??筌먲퐢沅?
   * @return ??諛댁뎽???熬곣뫁?????ㅻ쾴?????얜Ŧ堉??筌먲퐢沅?
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
   * ?リ옇?????ㅻ쾴????브퀗????????븐슙???띠룆????뿉????爰???源녿데????ㅻ쾴???筌먲퐢沅????瑜곸젧??類ｋ펲.
   * @param equipmentProcessId ??瑜곸젧????ㅻ쾴?????紐끒??
   * @param equipmentProcessUpdateRequest ??瑜곸젧????諛댄뀰 ??源녿데, ??ㅻ쾴???袁⑤?獄? ??ㅻ쾴??뗭춻??筌먲퐢沅?
   * @return ??瑜곸젧???熬곣뫁?????ㅻ쾴?????얜Ŧ堉??筌먲퐢沅?
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
   * ?リ옇?????ㅻ쾴????브퀗????????怨뺣뒆????????⑤객臾뜹슖??熬곥굦???類ｋ펲.
   * @param equipmentProcessId ???????ㅻ쾴?????紐끒??
   * @return ????嶺뚳퐣瑗?????ㅻ쾴?????얜Ŧ堉??筌먲퐢沅?
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