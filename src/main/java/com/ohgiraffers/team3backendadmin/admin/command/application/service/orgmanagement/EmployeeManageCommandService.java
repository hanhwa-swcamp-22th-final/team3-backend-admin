package com.ohgiraffers.team3backendadmin.admin.command.application.service.orgmanagement;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeDepartmentMatchRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.employee.EmployeeUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeCreateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeDepartmentMatchResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeDeleteResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.employee.EmployeeUpdateResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.org.OrgEmployeeTransferService;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.Skill;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.consent.Consent;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.SkillRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.ConsentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.service.OrganizationManageDomainService;
import com.ohgiraffers.team3backendadmin.common.constant.ConsentInfo;
import com.ohgiraffers.team3backendadmin.common.encryption.AesEncryptor;
import com.ohgiraffers.team3backendadmin.common.exception.AdminAccessDeniedException;
import com.ohgiraffers.team3backendadmin.common.exception.DepartmentNotFoundException;
import com.ohgiraffers.team3backendadmin.common.exception.DuplicateFieldException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.EmployeeSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.publisher.EmployeeReferenceEventPublisher;
import com.ohgiraffers.team3backendadmin.infrastructure.client.dto.HrMissionAssignmentRequest;
import com.ohgiraffers.team3backendadmin.infrastructure.client.feign.HrMissionFeignApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeManageCommandService {

    private final OrganizationManageDomainService organizationManageDomainService;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;
    private final ConsentRepository consentRepository;
    private final IdGenerator idGenerator;
    private final PasswordEncoder passwordEncoder;
    private final AesEncryptor aesEncryptor;
    private final EmployeeReferenceEventPublisher employeeReferenceEventPublisher;
    private final HrMissionFeignApi hrMissionFeignApi;
    private final OrgEmployeeTransferService orgEmployeeTransferService;

    // Insert Employee
    @Transactional
    public EmployeeCreateResponse insertEmployee(EmployeeCreateRequest request, String employeeCode) {

        employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(AdminAccessDeniedException::new);

        String encryptedEmail = aesEncryptor.encrypt(request.getEmployeeEmail());
        if (employeeRepository.existsByEmployeeEmail(encryptedEmail)) {
            throw new DuplicateFieldException("이미 사용중인 이메일 입니다");
        }

        String encryptedPhone = aesEncryptor.encrypt(request.getEmployeePhone());
        if (employeeRepository.existsByEmployeePhone(encryptedPhone)) {
            throw new DuplicateFieldException("이미 사용중인 전화번호 입니다");
        }

        String generatedCode = organizationManageDomainService.generateEmployeeCode();

        Employee employee = Employee.builder()
                .employeeId(idGenerator.generate())
                .departmentId(null)
                .employeeCode(generatedCode)
                .employeeName(request.getEmployeeName())
                .employeeEmail(encryptedEmail)
                .employeePhone(encryptedPhone)
                .employeeAddress(aesEncryptor.encrypt(request.getEmployeeAddress()))
                .employeeEmergencyContact(aesEncryptor.encrypt(request.getEmployeeEmergencyContact()))
                .employeePassword(passwordEncoder.encode(request.getEmployeePassword()))
                .employeeRole(request.getEmployeeRole())
                .employeeStatus(request.getEmployeeStatus())
                .employeeTier(request.getEmployeeTier())
                .hireDate(request.getHireDate())
                .build();

        employeeRepository.save(employee);
        publishEmployeeSnapshotAfterCommit(employee);
        assignNextTierMissionsAfterCommit(employee);

        // 새 사원 생성 시, 각 스킬 카테고리에 대해 레코드 생성 (총 6개, 사용자 입력값 또는 0)
        Arrays.stream(SkillCategory.values())
                .forEach(category -> {
                    Skill skill = Skill.builder()
                            .skillId(idGenerator.generate())
                            .employeeId(employee.getEmployeeId())
                            .skillCategory(category)
                            .skillScore(request.getScoreFor(category))
                            .evaluatedAt(LocalDateTime.now())
                            .build();
                    skillRepository.save(skill);
                });

        // 새 사원 생성 시, 약관 처리 테이블에 기본 레코드 생성.
        Consent defaultConsent = Consent.builder()
                .consentId(idGenerator.generate())
                .employeeId(employee.getEmployeeId())
                .consentVersion(ConsentInfo.CONSENT_VERSION)
                .isAgreed(false)
                .consentSavedPath(ConsentInfo.CONSENT_SAVED_PATH)
                .consentedAt(null)
                .build();

        consentRepository.save(defaultConsent);

        return EmployeeCreateResponse.builder()
                .employeeCode(generatedCode)
                .employeeName(request.getEmployeeName())
                .employeeEmail(request.getEmployeeEmail())
                .employeePhone(request.getEmployeePhone())
                .employeeAddress(request.getEmployeeAddress())
                .employeeEmergencyContact(request.getEmployeeEmergencyContact())
                .employeePassword(request.getEmployeePassword())
                .employeeRole(request.getEmployeeRole())
                .employeeStatus(request.getEmployeeStatus())
                .employeeTier(request.getEmployeeTier())
                .hireDate(request.getHireDate())
                .build();
    }

    // Delete employee
    @Transactional
    public EmployeeDeleteResponse deleteEmployee(String targetCode, String adminCode) {

        employeeRepository.findByEmployeeCode(adminCode)
                .orElseThrow(AdminAccessDeniedException::new);

        Employee target = employeeRepository.findByEmployeeCode(targetCode)
                .orElseThrow(EmployeeNotFoundException::new);

        EmployeeDeleteResponse response = EmployeeDeleteResponse.builder()
                .employeeCode(target.getEmployeeCode())
                .employeeName(target.getEmployeeName())
                .build();

        target.deleteEmployee();
        publishEmployeeSnapshotAfterCommit(target);

        return response;
    }

    // Match department to employee
    @Transactional
    public EmployeeDepartmentMatchResponse matchDepartment(EmployeeDepartmentMatchRequest request, String hrmCode) {

        employeeRepository.findByEmployeeCode(hrmCode)
                .orElseThrow(AdminAccessDeniedException::new);

        Employee target = employeeRepository.findByEmployeeCode(request.getEmployeeCode())
                .orElseThrow(EmployeeNotFoundException::new);

        departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(DepartmentNotFoundException::new);

        orgEmployeeTransferService.transfer(target.getEmployeeId(), request.getDepartmentId());

        return EmployeeDepartmentMatchResponse.builder()
                .employeeName(target.getEmployeeName())
                .employeeCode(target.getEmployeeCode())
                .departmentId(request.getDepartmentId())
                .build();
    }

    // Update Employee
    @Transactional
    public EmployeeUpdateResponse updateEmployee(EmployeeUpdateRequest request, String adminCode) {

        employeeRepository.findByEmployeeCode(adminCode)
                .orElseThrow(AdminAccessDeniedException::new);

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(EmployeeNotFoundException::new);

        // 이메일 변경 시 중복 체크
        String encryptedEmail = null;
        if (request.getEmployeeEmail() != null) {
            encryptedEmail = aesEncryptor.encrypt(request.getEmployeeEmail());
            if (employeeRepository.existsByEmployeeEmailAndEmployeeIdNot(encryptedEmail, employee.getEmployeeId())) {
                throw new DuplicateFieldException("이미 사용중인 이메일 입니다");
            }
        }

        // 전화번호 변경 시 중복 체크
        String encryptedPhone = null;
        if (request.getEmployeePhone() != null) {
            encryptedPhone = aesEncryptor.encrypt(request.getEmployeePhone());
            if (employeeRepository.existsByEmployeePhoneAndEmployeeIdNot(encryptedPhone, employee.getEmployeeId())) {
                throw new DuplicateFieldException("이미 사용중인 전화번호 입니다");
            }
        }

        // 비밀번호 인코딩
        String encodedPassword = request.getEmployeePassword() != null
                ? passwordEncoder.encode(request.getEmployeePassword())
                : null;

        // 암호화 처리된 값으로 엔티티 업데이트
        employee.updateByAdmin(
                request.getEmployeeName(),
                encryptedEmail,
                encryptedPhone,
                request.getEmployeeAddress() != null ? aesEncryptor.encrypt(request.getEmployeeAddress()) : null,
                request.getEmployeeEmergencyContact() != null ? aesEncryptor.encrypt(request.getEmployeeEmergencyContact()) : null,
                encodedPassword,
                request.getEmployeeRole(),
                request.getEmployeeStatus(),
                request.getEmployeeTier(),
                request.getHireDate()
        );

        // 스킬 점수 업데이트 (non-null인 항목만)
        Arrays.stream(SkillCategory.values())
                .forEach(category -> {
                    BigDecimal score = request.getScoreFor(category);
                    if (score != null) {
                        skillRepository.findByEmployeeIdAndSkillCategory(employee.getEmployeeId(), category)
                                .ifPresent(skill -> skill.updateScore(score));
                    }
                });

        publishEmployeeSnapshotAfterCommit(employee);
        if (request.getEmployeeTier() != null) {
            assignNextTierMissionsAfterCommit(employee);
        }

        return EmployeeUpdateResponse.builder()
                .employeeId(employee.getEmployeeId())
                .employeeCode(employee.getEmployeeCode())
                .employeeName(employee.getEmployeeName())
                .employeeEmail(request.getEmployeeEmail() != null ? request.getEmployeeEmail() : aesEncryptor.decrypt(employee.getEmployeeEmail()))
                .employeePhone(request.getEmployeePhone() != null ? request.getEmployeePhone() : aesEncryptor.decrypt(employee.getEmployeePhone()))
                .employeeAddress(request.getEmployeeAddress() != null ? request.getEmployeeAddress() : aesEncryptor.decrypt(employee.getEmployeeAddress()))
                .employeeEmergencyContact(request.getEmployeeEmergencyContact() != null ? request.getEmployeeEmergencyContact() : aesEncryptor.decrypt(employee.getEmployeeEmergencyContact()))
                .employeeRole(employee.getEmployeeRole())
                .employeeStatus(employee.getEmployeeStatus())
                .employeeTier(employee.getEmployeeTier())
                .hireDate(employee.getHireDate())
                .build();
    }

    @Transactional
    public void updateEmployeeTier(Long employeeId, EmployeeTier employeeTier) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(EmployeeNotFoundException::new);

        employee.updateTier(employeeTier);
        publishEmployeeSnapshotAfterCommit(employee);
        assignNextTierMissionsAfterCommit(employee);
    }

    private void publishEmployeeSnapshotAfterCommit(Employee employee) {
        EmployeeSnapshotEvent event = EmployeeSnapshotEvent.builder()
            .employeeId(employee.getEmployeeId())
            .employeeCode(employee.getEmployeeCode())
            .employeeTier(employee.getEmployeeTier() == null ? null : employee.getEmployeeTier().name())
            .employeeStatus(employee.getEmployeeStatus() == null ? null : employee.getEmployeeStatus().name())
            .occurredAt(LocalDateTime.now())
            .build();

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    employeeReferenceEventPublisher.publishEmployeeSnapshot(event);
                }
            });
            return;
        }

        employeeReferenceEventPublisher.publishEmployeeSnapshot(event);
    }

    private void assignNextTierMissionsAfterCommit(Employee employee) {
        if (employee.getEmployeeRole() != EmployeeRole.WORKER || employee.getEmployeeTier() == null) {
            return;
        }

        Runnable action = () -> {
            try {
                hrMissionFeignApi.assignNextTierMissions(HrMissionAssignmentRequest.builder()
                        .employeeId(employee.getEmployeeId())
                        .currentTier(employee.getEmployeeTier().name())
                        .build());
            } catch (Exception e) {
                log.warn(
                        "HR mission assignment failed. employeeId={}, currentTier={}",
                        employee.getEmployeeId(),
                        employee.getEmployeeTier(),
                        e
                );
            }
        };

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    action.run();
                }
            });
            return;
        }

        action.run();
    }
}
