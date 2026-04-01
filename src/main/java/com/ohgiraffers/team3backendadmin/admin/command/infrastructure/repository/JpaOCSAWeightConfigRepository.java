package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.ocsaweightconfig.OCSAWeightConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOCSAWeightConfigRepository extends JpaRepository<OCSAWeightConfig, Long> {
    boolean existsByIndustryPresetName(String industryPresetName);
    boolean existsByIndustryPresetNameAndConfigIdNot(String industryPresetName, Long configId);
}
