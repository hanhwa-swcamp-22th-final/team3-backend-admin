package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EmployeeCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.EmployeeUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.Skill;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.SkillRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.service.OrganizationManageDomainService;
import com.ohgiraffers.team3backendadmin.common.encryption.AesEncryptor;
import com.ohgiraffers.team3backendadmin.common.exception.AdminAccessDeniedException;
import com.ohgiraffers.team3backendadmin.common.exception.DepartmentNotFoundException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class EmployeeManageCommandService {

    private final OrganizationManageDomainService organizationManageDomainService;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;
    private final IdGenerator idGenerator;
    private final PasswordEncoder passwordEncoder;
    private final AesEncryptor aesEncryptor;

    // Insert Employee
    @Transactional
    public void insertEmployee(EmployeeCreateRequest request, String employeeCode) {

        employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(AdminAccessDeniedException::new);

        departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(DepartmentNotFoundException::new);

        String generatedCode = organizationManageDomainService.generateEmployeeCode();

        Employee employee = Employee.builder()
                .employeeId(idGenerator.generate())
                .departmentId(request.getDepartmentId())
                .employeeCode(generatedCode)
                .employeeName(request.getEmployeeName())
                .employeeEmail(aesEncryptor.encrypt(request.getEmployeeEmail()))
                .employeePhone(aesEncryptor.encrypt(request.getEmployeePhone()))
                .employeeAddress(aesEncryptor.encrypt(request.getEmployeeAddress()))
                .employeeEmergencyContact(aesEncryptor.encrypt(request.getEmployeeEmergencyContact()))
                .employeePassword(passwordEncoder.encode(request.getEmployeePassword()))
                .employeeRole(request.getEmployeeRole())
                .employeeStatus(request.getEmployeeStatus())
                .employeeTier(request.getEmployeeTier())
                .build();

        employeeRepository.save(employee);

        // 새 사원 생성 시, 각 스킬 카테고리에 대해 기본 레코드 생성 (총 6개)
        Arrays.stream(SkillCategory.values())
                .forEach(category -> {
                    Skill defaultSkill = Skill.builder()
                            .skillId(idGenerator.generate())
                            .employeeId(employee.getEmployeeId())
                            .skillCategory(category)
                            .skillScore(BigDecimal.ZERO)
                            .evaluatedAt(LocalDateTime.now())
                            .build();
                    skillRepository.save(defaultSkill);
                });
    }

    // Update Employee
    @Transactional
    public void updateEmployee(EmployeeUpdateRequest request, String employeeCode) {

        employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(AdminAccessDeniedException::new);

        Employee target = employeeRepository.findByEmployeeCode(request.getEmployeeCode())
                .orElseThrow(EmployeeNotFoundException::new);

        target.updatePersonalInfo(
                request.getEmployeeName(),
                request.getEmployeeEmail() != null ? aesEncryptor.encrypt(request.getEmployeeEmail()) : null,
                request.getEmployeePhone() != null ? aesEncryptor.encrypt(request.getEmployeePhone()) : null,
                request.getEmployeeAddress() != null ? aesEncryptor.encrypt(request.getEmployeeAddress()) : null,
                request.getEmployeeEmergencyContact() != null ? aesEncryptor.encrypt(request.getEmployeeEmergencyContact()) : null
        );
    }

    // Delete employee
    @Transactional
    public void deleteEmployee(String targetCode, String adminCode) {

        employeeRepository.findByEmployeeCode(adminCode)
                .orElseThrow(AdminAccessDeniedException::new);

        Employee target = employeeRepository.findByEmployeeCode(targetCode)
                .orElseThrow(EmployeeNotFoundException::new);

        target.deleteEmployee();
    }
}
