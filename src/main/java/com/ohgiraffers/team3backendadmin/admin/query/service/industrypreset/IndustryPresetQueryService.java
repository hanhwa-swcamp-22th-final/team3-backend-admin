package com.ohgiraffers.team3backendadmin.admin.query.service.industrypreset;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.OCSAWeightConfigResponse;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.OCSAWeightConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndustryPresetQueryService {

    private final OCSAWeightConfigMapper ocsaWeightConfigMapper;

    public List<OCSAWeightConfigResponse> getAllPresets() {
        return ocsaWeightConfigMapper.findAll();
    }
}
