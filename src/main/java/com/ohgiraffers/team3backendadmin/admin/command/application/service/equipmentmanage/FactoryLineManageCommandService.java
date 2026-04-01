package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
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
public class FactoryLineManageCommandService {

  private final FactoryLineRepository factoryLineRepository;
  private final IdGenerator idGenerator;

  /**
   * 생산 라인 생성 요청 정보를 기반으로 중복 코드를 확인한 뒤 새로운 생산 라인을 등록한다.
   * @param factoryLineCreateRequest 생산 라인 생성에 필요한 코드와 이름 정보
   * @return 생성된 생산 라인의 식별자와 기본 정보
   */
  public FactoryLineCreateResponse createFactoryLine(FactoryLineCreateRequest factoryLineCreateRequest){
    if (factoryLineRepository.findByFactoryLineCode(factoryLineCreateRequest.getFactoryLineCode()).isPresent()) {
      throw new BusinessException(ErrorCode.FACTORY_LINE_CODE_ALREADY_EXISTS);
    }

    FactoryLine factoryLine = FactoryLine.builder()
        .factoryLineId(idGenerator.generate())
        .factoryLineCode(factoryLineCreateRequest.getFactoryLineCode())
        .factoryLineName(factoryLineCreateRequest.getFactoryLineName())
        .build();

    factoryLineRepository.save(factoryLine);

    return FactoryLineCreateResponse.builder()
        .factoryLineId(factoryLine.getFactoryLineId())
        .factoryLineCode(factoryLine.getFactoryLineCode())
        .factoryLineName(factoryLine.getFactoryLineName())
        .build();
  }

  /**
   * 기존 생산 라인을 조회하고 자기 자신을 제외한 코드 중복 여부를 확인한 뒤 요청 정보로 수정한다.
   * @param factoryLindId 수정할 생산 라인 식별자
   * @param factoryLineUpdateRequest 수정할 생산 라인 코드와 이름 정보
   * @return 수정된 생산 라인의 식별자와 기본 정보
   */
  public FactoryLineUpdateResponse updateFactoryLine(Long factoryLindId, FactoryLineUpdateRequest factoryLineUpdateRequest){

    FactoryLine factoryLine = factoryLineRepository.findById(factoryLindId).orElseThrow(
        ()-> new BusinessException(ErrorCode.FACTORY_LINE_NOT_FOUND)
    );

    factoryLineRepository.findByFactoryLineCode(factoryLineUpdateRequest.getFactoryLineCode())
        .filter(found -> !found.getFactoryLineId().equals(factoryLindId))
        .ifPresent(found -> {
          throw new BusinessException(ErrorCode.FACTORY_LINE_CODE_ALREADY_EXISTS);
        });

    factoryLine.updateInfo(
        factoryLineUpdateRequest.getFactoryLineCode(),
        factoryLineUpdateRequest.getFactoryLineName());

    return FactoryLineUpdateResponse.builder()
        .factoryLineId(factoryLine.getFactoryLineId())
        .factoryLineCode(factoryLine.getFactoryLineCode())
        .factoryLineName(factoryLine.getFactoryLineName())
        .build();
  }

  /**
   * 기존 생산 라인을 조회한 뒤 소프트 삭제 처리한다.
   * @param factoryLindId 삭제할 생산 라인 식별자
   * @return 삭제 처리된 생산 라인의 식별자와 기본 정보
   */
  public FactoryLineUpdateResponse deleteFactoryLine(Long factoryLindId){

    FactoryLine factoryLine = factoryLineRepository.findById(factoryLindId).orElseThrow(
        ()-> new BusinessException(ErrorCode.FACTORY_LINE_NOT_FOUND)
    );

    factoryLine.softDelete();

    return FactoryLineUpdateResponse.builder()
        .factoryLineId(factoryLine.getFactoryLineId())
        .factoryLineCode(factoryLine.getFactoryLineCode())
        .factoryLineName(factoryLine.getFactoryLineName())
        .build();
  }
}
