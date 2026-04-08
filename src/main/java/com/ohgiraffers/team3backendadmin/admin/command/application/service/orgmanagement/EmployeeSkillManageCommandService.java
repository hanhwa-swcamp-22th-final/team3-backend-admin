package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeSkillUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeSkillUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.Skill;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.SkillRepository;
import com.ohgiraffers.team3backendadmin.common.exception.AdminAccessDeniedException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import com.ohgiraffers.team3backendadmin.common.exception.SkillNotFoundException;
import lombok.RequiredArgsConstructor;
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
    public EmployeeSkillUpdateResponse updateEmployeeSkill(EmployeeSkillUpdateRequest request, String adminCode) {

        employeeRepository.findByEmployeeCode(adminCode)
                .orElseThrow(AdminAccessDeniedException::new);

        Employee target = employeeRepository.findByEmployeeCode(request.getEmployeeCode())
                .orElseThrow(EmployeeNotFoundException::new);

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
                ).orElseThrow(() -> new SkillNotFoundException(
                        "해당 스킬 레코드를 찾을 수 없습니다: " + category
                ));
                skill.updateScore(score);
            }
        });

        return EmployeeSkillUpdateResponse.builder()
                .employeeCode(request.getEmployeeCode())
                .equipmentResponse(request.getEquipmentResponse())
                .technicalTransfer(request.getTechnicalTransfer())
                .innovationProposal(request.getInnovationProposal())
                .safetyCompliance(request.getSafetyCompliance())
                .qualityManagement(request.getQualityManagement())
                .productivity(request.getProductivity())
                .build();
    }
}
