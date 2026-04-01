package com.ohgiraffers.team3backendadmin.admin.query.controller;

import com.ohgiraffers.team3backendadmin.admin.query.service.industrypreset.IndustryPresetQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IndustryPresetQueryController {

    private final IndustryPresetQueryService industryPresetQueryService;


}
