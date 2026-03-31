package com.ohgiraffers.team3backendadmin.admin.command.application.service;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.FactoryLine;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.FactoryLineRepository;
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
   * 생산 라인 코드 중복 여부를 확인한 뒤 신규 생산 라인을 저장한다.
   * @param factoryLineCreateRequest 생성할 생산 라인의 코드와 이름 값
   * @return 생성 완료된 생산 라인 정보를 담은 응답 값
   */
  public FactoryLineCreateResponse createFactoryLine(FactoryLineCreateRequest factoryLineCreateRequest){
    if (factoryLineRepository.findByFactoryLineCode(factoryLineCreateRequest.getFactoryLineCode()).isPresent()) {
      throw new IllegalArgumentException("Factory line code already exists");
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
   * 기존 생산 라인을 조회한 뒤 요청 값으로 코드와 이름을 변경한다.
   * @param factoryLindId 수정 대상 생산 라인의 식별자
   * @param factoryLineUpdateRequest 수정할 생산 라인 코드와 이름 값
   * @return 수정 완료된 생산 라인 정보를 담은 응답 값
   */
  public FactoryLineUpdateResponse updateFactoryLine(Long factoryLindId, FactoryLineUpdateRequest factoryLineUpdateRequest){

    FactoryLine factoryLine = factoryLineRepository.findById(factoryLindId).orElseThrow(
        ()-> new IllegalArgumentException("Factory line not found.")
    );

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
   * 기존 생산 라인을 조회한 뒤 소프트 삭제 상태로 전환한다.
   * @param factoryLindId 삭제 대상 생산 라인의 식별자
   * @return 삭제 처리 후 생산 라인의 최신 상태를 담은 응답 값
   */
  public FactoryLineUpdateResponse deleteFactoryLine(Long factoryLindId){

    FactoryLine factoryLine = factoryLineRepository.findById(factoryLindId).orElseThrow(
        ()-> new IllegalArgumentException("Factory line not found.")
    );

    factoryLine.softDelete();

    return FactoryLineUpdateResponse.builder()
        .factoryLineId(factoryLine.getFactoryLineId())
        .factoryLineCode(factoryLine.getFactoryLineCode())
        .factoryLineName(factoryLine.getFactoryLineName())
        .build();
  }
}
