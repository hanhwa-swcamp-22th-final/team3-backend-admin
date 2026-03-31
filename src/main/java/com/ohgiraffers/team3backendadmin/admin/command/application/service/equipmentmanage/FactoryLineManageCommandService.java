package com.ohgiraffers.team3backendadmin.admin.command.application.service.equipmentmanage;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.FactoryLineUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.FactoryLineUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.equipment.FactoryLine;
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
   * ?앹궛 ?쇱씤 肄붾뱶 以묐났 ?щ?瑜??뺤씤?섍퀬 ?좉퇋 ?앹궛 ?쇱씤???앹꽦?쒕떎.
   * @param factoryLineCreateRequest ?앹꽦???앹궛 ?쇱씤??肄붾뱶? ?대쫫 ?뺣낫
   * @return ?앹꽦???꾨즺???앹궛 ?쇱씤???묐떟 ?뺣낫
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
   * 湲곗〈 ?앹궛 ?쇱씤??議고쉶?????붿껌 媛믪쑝濡?肄붾뱶? ?대쫫???섏젙?쒕떎.
   * @param factoryLindId ?섏젙???앹궛 ?쇱씤???앸퀎??
   * @param factoryLineUpdateRequest ?섏젙???앹궛 ?쇱씤??肄붾뱶? ?대쫫 ?뺣낫
   * @return ?섏젙???꾨즺???앹궛 ?쇱씤???묐떟 ?뺣낫
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
   * 湲곗〈 ?앹궛 ?쇱씤??議고쉶?????뚰봽????젣 ?곹깭濡??꾪솚?쒕떎.
   * @param factoryLindId ??젣???앹궛 ?쇱씤???앸퀎??
   * @return ??젣 泥섎━???앹궛 ?쇱씤???묐떟 ?뺣낫
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