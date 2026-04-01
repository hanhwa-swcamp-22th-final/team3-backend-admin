package com.ohgiraffers.team3backendadmin.admin.query.service;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.OCSAWeightConfigResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.OCSAWeightConfigMapper;
import com.ohgiraffers.team3backendadmin.admin.query.service.industrypreset.IndustryPresetQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class IndustryPresetQueryServiceTest {

    @InjectMocks
    private IndustryPresetQueryService industryPresetQueryService;

    @Mock
    private OCSAWeightConfigMapper ocsaWeightConfigMapper;

    @Nested
    @DisplayName("getAllPresets 메서드")
    class GetAllPresets {

        @Test
        @DisplayName("전체 프리셋 목록을 조회한다")
        void getAllPresetsSuccess() {
            // given
            List<OCSAWeightConfigResponse> presets = List.of(
                    new OCSAWeightConfigResponse(),
                    new OCSAWeightConfigResponse()
            );
            given(ocsaWeightConfigMapper.findAll()).willReturn(presets);

            // when
            List<OCSAWeightConfigResponse> result = industryPresetQueryService.getAllPresets();

            // then
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("프리셋이 없으면 빈 리스트를 반환한다")
        void getAllPresetsEmpty() {
            // given
            given(ocsaWeightConfigMapper.findAll()).willReturn(List.of());

            // when
            List<OCSAWeightConfigResponse> result = industryPresetQueryService.getAllPresets();

            // then
            assertTrue(result.isEmpty());
        }
    }
}
