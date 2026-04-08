package com.ohgiraffers.team3backendadmin.admin.command.application.service.industrypreset;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.industrypreset.IndustryPresetCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.industrypreset.IndustryPresetDeleteRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.industrypreset.IndustryPresetUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.industrypreset.IndustryPresetCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.industrypreset.IndustryPresetDeleteResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.industrypreset.IndustryPresetUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.ocsaweightconfig.OCSAWeightConfig;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.OCSAWeightConfigRepository;
import com.ohgiraffers.team3backendadmin.common.exception.DuplicateFieldException;
import com.ohgiraffers.team3backendadmin.common.exception.InvalidInputException;
import com.ohgiraffers.team3backendadmin.common.exception.OCSAWeightConfigNotFoundException;
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
    public IndustryPresetCreateResponse create(IndustryPresetCreateRequest request) {

        if (ocsaWeightConfigRepository.existsByIndustryPresetName(request.getIndustryPresetName())) {
            throw new DuplicateFieldException("이미 사용 중인 프리셋 이름입니다.");
        }

        validateWeights(request.getWeightV1(), request.getWeightV2(),
                request.getWeightV3(), request.getWeightV4(), request.getAlphaWeight());

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

        return IndustryPresetCreateResponse.builder()
                .industryPresetName(config.getIndustryPresetName())
                .weightV1(config.getWeightV1())
                .weightV2(config.getWeightV2())
                .weightV3(config.getWeightV3())
                .weightV4(config.getWeightV4())
                .alphaWeight(config.getAlphaWeight())
                .effectiveDate(config.getEffectiveDate())
                .build();
    }

    @Transactional
    public IndustryPresetUpdateResponse update(IndustryPresetUpdateRequest request) {

        OCSAWeightConfig config = ocsaWeightConfigRepository.findById(request.getConfigId())
                .orElseThrow(OCSAWeightConfigNotFoundException::new);

        if (request.getIndustryPresetName() != null
                && ocsaWeightConfigRepository.existsByIndustryPresetNameAndConfigIdNot(
                request.getIndustryPresetName(), config.getConfigId())) {
            throw new DuplicateFieldException("이미 사용 중인 프리셋 이름입니다.");
        }

        BigDecimal mergedV1 = request.getWeightV1() != null ? request.getWeightV1() : config.getWeightV1();
        BigDecimal mergedV2 = request.getWeightV2() != null ? request.getWeightV2() : config.getWeightV2();
        BigDecimal mergedV3 = request.getWeightV3() != null ? request.getWeightV3() : config.getWeightV3();
        BigDecimal mergedV4 = request.getWeightV4() != null ? request.getWeightV4() : config.getWeightV4();
        BigDecimal mergedAlpha = request.getAlphaWeight() != null ? request.getAlphaWeight() : config.getAlphaWeight();

        validateWeights(mergedV1, mergedV2, mergedV3, mergedV4, mergedAlpha);

        config.updatePreset(
                request.getIndustryPresetName(),
                request.getWeightV1(),
                request.getWeightV2(),
                request.getWeightV3(),
                request.getWeightV4(),
                request.getAlphaWeight(),
                request.getEffectiveDate()
        );

        return IndustryPresetUpdateResponse.builder()
                .configId(config.getConfigId())
                .industryPresetName(config.getIndustryPresetName())
                .weightV1(config.getWeightV1())
                .weightV2(config.getWeightV2())
                .weightV3(config.getWeightV3())
                .weightV4(config.getWeightV4())
                .alphaWeight(config.getAlphaWeight())
                .effectiveDate(config.getEffectiveDate())
                .build();
    }

    @Transactional
    public IndustryPresetDeleteResponse delete(IndustryPresetDeleteRequest request) {

        OCSAWeightConfig config = ocsaWeightConfigRepository.findById(request.getConfigId())
                .orElseThrow(OCSAWeightConfigNotFoundException::new);

        IndustryPresetDeleteResponse response = IndustryPresetDeleteResponse.builder()
                .configId(config.getConfigId())
                .industryPresetName(config.getIndustryPresetName())
                .build();

        config.softDelete();

        return response;
    }

    private void validateWeights(BigDecimal v1, BigDecimal v2, BigDecimal v3, BigDecimal v4,
                                 BigDecimal alphaWeight) {
        BigDecimal weightSum = v1.add(v2).add(v3).add(v4);

        if (weightSum.compareTo(BigDecimal.ONE) != 0) {
            throw new InvalidInputException("가중치(v1~v4)의 합은 1.00이어야 합니다.");
        }

        if (alphaWeight.compareTo(BigDecimal.ZERO) < 0
                || alphaWeight.compareTo(new BigDecimal("0.5")) > 0) {
            throw new InvalidInputException("알파 가중치는 0.0~0.5 범위여야 합니다.");
        }
    }
}
