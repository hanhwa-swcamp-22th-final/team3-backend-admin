package com.ohgiraffers.team3backendadmin.admin.command.application.service.industrypreset;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.IndustryPresetCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.ocsaweightconfig.OCSAWeightConfig;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.OCSAWeightConfigRepository;
import com.ohgiraffers.team3backendadmin.common.exception.DuplicateFieldException;
import com.ohgiraffers.team3backendadmin.common.exception.InvalidInputException;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class IndustryPresetCommandService {

    private final OCSAWeightConfigRepository ocsaWeightConfigRepository;
    private final IdGenerator idGenerator;

    @Transactional
    public void create(IndustryPresetCreateRequest request) {

        if (ocsaWeightConfigRepository.existsByIndustryPresetName(request.getIndustryPresetName())) {
            throw new DuplicateFieldException("이미 사용 중인 프리셋 이름입니다.");
        }

        BigDecimal weightSum = request.getWeightV1()
                .add(request.getWeightV2())
                .add(request.getWeightV3())
                .add(request.getWeightV4());

        if (weightSum.compareTo(BigDecimal.ONE) != 0) {
            throw new InvalidInputException("가중치(v1~v4)의 합은 1.00이어야 합니다.");
        }

        if (request.getAlphaWeight().compareTo(BigDecimal.ZERO) < 0
                || request.getAlphaWeight().compareTo(new BigDecimal("0.5")) > 0) {
            throw new InvalidInputException("알파 가중치는 0.0~0.5 범위여야 합니다.");
        }

        OCSAWeightConfig config = OCSAWeightConfig.builder()
                .configId(idGenerator.generate())
                .industryPresetName(request.getIndustryPresetName())
                .weightV1(request.getWeightV1())
                .weightV2(request.getWeightV2())
                .weightV3(request.getWeightV3())
                .weightV4(request.getWeightV4())
                .alphaWeight(request.getAlphaWeight())
                .effectiveDate(request.getEffectiveDate())
                .build();

        ocsaWeightConfigRepository.save(config);
    }
}
