package com.ohgiraffers.team3backendadmin.infrastructure.client.feign;

import com.ohgiraffers.team3backendadmin.infrastructure.client.dto.HrMissionAssignmentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "hrMissionFeignApi",
    url = "${feign.hr.url:http://localhost:8081}",
    configuration = HrFeignConfiguration.class
)
public interface HrMissionFeignApi {

    @PostMapping("/api/v1/hr/missions/assign-next-tier")
    void assignNextTierMissions(@RequestBody HrMissionAssignmentRequest request);
}
