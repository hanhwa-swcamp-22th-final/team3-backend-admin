package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.algorithmversion.AlgorithmVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlgorithmVersionRepository extends JpaRepository<AlgorithmVersion, Long> {

    boolean existsByVersionNo(String versionNo);

    boolean existsByImplementationKey(String implementationKey);

    boolean existsByVersionNoAndAlgorithmVersionIdNot(String versionNo, Long algorithmVersionId);

    boolean existsByImplementationKeyAndAlgorithmVersionIdNot(String implementationKey, Long algorithmVersionId);
}