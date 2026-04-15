package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.score.Score;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findTopByEmployeeIdAndEvalPeriodIdOrderByScoreIdDesc(Long employeeId, Long evalPeriodId);
}
