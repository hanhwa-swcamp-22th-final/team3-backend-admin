package com.ohgiraffers.team3backendadmin.admin.command.application.service.score;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.score.Score;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.Skill;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.ScoreRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.SkillRepository;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.PerformancePointSnapshotEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScoreSnapshotCommandService {

    private static final Logger log = LoggerFactory.getLogger(ScoreSnapshotCommandService.class);
    private static final BigDecimal PROMOTION_POINT_NORMALIZER = BigDecimal.valueOf(10_000);
    private static final String SETTLEMENT_SOURCE_TYPE = "EVALUATION_PERIOD_SETTLEMENT";

    private final ScoreRepository scoreRepository;
    private final SkillRepository skillRepository;
    private final EmployeeRepository employeeRepository;
    private final IdGenerator idGenerator;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void refreshAfterSkillGrowth(Long employeeId, Long evaluationPeriodId) {
        refreshSnapshot(employeeId, evaluationPeriodId, true, true);
    }

    @Transactional
    public void refreshAfterPerformancePointSnapshot(PerformancePointSnapshotEvent event) {
        if (event == null) {
            return;
        }
        if (!SETTLEMENT_SOURCE_TYPE.equals(event.getPointSourceType())) {
            log.info(
                "Skipping score snapshot refresh for non-settlement point event. employeeId={}, pointSourceType={}, pointSourceId={}",
                event.getEmployeeId(),
                event.getPointSourceType(),
                event.getPointSourceId()
            );
            return;
        }
        refreshSnapshot(event.getEmployeeId(), event.getPointSourceId(), true, true);
    }

    private void refreshSnapshot(
        Long employeeId,
        Long evaluationPeriodId,
        boolean refreshCapabilityIndex,
        boolean refreshTotalPoints
    ) {
        if (employeeId == null || evaluationPeriodId == null) {
            log.info(
                "Skipping score snapshot refresh because key is missing. employeeId={}, evaluationPeriodId={}",
                employeeId,
                evaluationPeriodId
            );
            return;
        }

        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            log.info("Skipping score snapshot refresh because employee was not found. employeeId={}", employeeId);
            return;
        }

        BigDecimal capabilityIndex = refreshCapabilityIndex ? calculateCapabilityIndex(employeeId) : null;
        Integer totalPoints = refreshTotalPoints ? calculateTotalPoints(employeeId) : null;

        Score score = scoreRepository
            .findTopByEmployeeIdAndEvalPeriodIdOrderByScoreIdDesc(employeeId, evaluationPeriodId)
            .orElseGet(() -> Score.create(
                idGenerator.generate(),
                employeeId,
                evaluationPeriodId,
                null,
                null,
                null
            ));

        score.updateSnapshot(capabilityIndex, totalPoints, employee.getEmployeeTier());
        scoreRepository.save(score);

        log.info(
            "Refreshed score snapshot. employeeId={}, evaluationPeriodId={}, capabilityIndex={}, totalPoints={}, tier={}",
            employeeId,
            evaluationPeriodId,
            capabilityIndex,
            totalPoints,
            employee.getEmployeeTier()
        );
    }

    private BigDecimal calculateCapabilityIndex(Long employeeId) {
        List<Skill> skills = skillRepository.findByEmployeeId(employeeId).stream()
            .filter(skill -> skill.getSkillScore() != null)
            .toList();

        if (skills.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal total = skills.stream()
            .map(Skill::getSkillScore)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(skills.size()), 2, RoundingMode.HALF_UP);
    }

    private Integer calculateTotalPoints(Long employeeId) {
        BigDecimal rawTotal = jdbcTemplate.queryForObject(
            """
                SELECT COALESCE(SUM(point_amount), 0)
                  FROM performance_point
                 WHERE performance_employee_id = ?
                """,
            BigDecimal.class,
            employeeId
        );

        BigDecimal normalized = (rawTotal == null ? BigDecimal.ZERO : rawTotal)
            .divide(PROMOTION_POINT_NORMALIZER, 0, RoundingMode.HALF_UP);

        return normalized.intValue();
    }
}
