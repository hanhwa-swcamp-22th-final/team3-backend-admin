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
