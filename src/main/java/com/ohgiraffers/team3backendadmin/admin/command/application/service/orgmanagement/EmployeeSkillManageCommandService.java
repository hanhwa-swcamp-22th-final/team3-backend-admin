package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EmployeeSkillUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.Skill;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeSkillManageCommandService {

    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;

    @Transactional
    public void updateEmployeeSkill(EmployeeSkillUpdateRequest request, String adminCode) {

        employeeRepository.findByEmployeeCode(adminCode)
                .orElseThrow(() -> new BadCredentialsException("해당 사원 정보를 찾을 수 없습니다"));

        Employee target = employeeRepository.findByEmployeeCode(request.getEmployeeCode())
                .orElseThrow(() -> new IllegalArgumentException("해당 사원을 찾을 수 없습니다"));

        Map<SkillCategory, BigDecimal> scoreMap = new LinkedHashMap<>();
        scoreMap.put(SkillCategory.EQUIPMENT_RESPONSE, request.getEquipmentResponse());
        scoreMap.put(SkillCategory.TECHNICAL_TRANSFER, request.getTechnicalTransfer());
        scoreMap.put(SkillCategory.INNOVATION_PROPOSAL, request.getInnovationProposal());
        scoreMap.put(SkillCategory.SAFETY_COMPLIANCE, request.getSafetyCompliance());
        scoreMap.put(SkillCategory.QUALITY_MANAGEMENT, request.getQualityManagement());
        scoreMap.put(SkillCategory.PRODUCTIVITY, request.getProductivity());

        scoreMap.forEach((category, score) -> {
            if (score != null) {
                Skill skill = skillRepository.findByEmployeeIdAndSkillCategory(
                        target.getEmployeeId(), category
                ).orElseThrow(() -> new IllegalArgumentException(
                        "해당 스킬 레코드를 찾을 수 없습니다: " + category
                ));
                skill.updateScore(score);
            }
        });
    }
}
