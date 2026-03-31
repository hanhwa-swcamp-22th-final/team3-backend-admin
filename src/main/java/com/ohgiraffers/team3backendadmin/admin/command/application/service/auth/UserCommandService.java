package com.ohgiraffers.team3backendadmin.admin.command.application.service.auth;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.PasswordChangeRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.ProfileUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.passwordhistory.PasswordHistory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.PasswordHistoryRepository;
import com.ohgiraffers.team3backendadmin.common.encryption.AesEncryptor;
import com.ohgiraffers.team3backendadmin.common.exception.DuplicateFieldException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import com.ohgiraffers.team3backendadmin.common.exception.PasswordMismatchException;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private static final int MAX_PASSWORD_HISTORY = 3;

    private final EmployeeRepository employeeRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;
    private final AesEncryptor aesEncryptor;

    @Transactional
    public void updateProfile(ProfileUpdateRequest request, String employeeCode) {

        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(EmployeeNotFoundException::new);

        if (request.getEmployeeEmail() != null) {
            String encryptedEmail = aesEncryptor.encrypt(request.getEmployeeEmail());
            if (employeeRepository.existsByEmployeeEmail(encryptedEmail)) {
                throw new DuplicateFieldException("이미 사용중인 이메일 입니다");
            }
        }

        if (request.getEmployeePhone() != null) {
            String encryptedPhone = aesEncryptor.encrypt(request.getEmployeePhone());
            if (employeeRepository.existsByEmployeePhone(encryptedPhone)) {
                throw new DuplicateFieldException("이미 사용중인 전화번호 입니다");
            }
        }

        employee.updatePersonalInfo(
                request.getEmployeeName(),
                request.getEmployeeEmail() != null ? aesEncryptor.encrypt(request.getEmployeeEmail()) : null,
                request.getEmployeePhone() != null ? aesEncryptor.encrypt(request.getEmployeePhone()) : null,
                request.getEmployeeAddress() != null ? aesEncryptor.encrypt(request.getEmployeeAddress()) : null,
                request.getEmployeeEmergencyContact() != null ? aesEncryptor.encrypt(request.getEmployeeEmergencyContact()) : null
        );
    }

    @Transactional
    public void changePassword(PasswordChangeRequest request, String employeeCode) {

        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(EmployeeNotFoundException::new);

        if (!passwordEncoder.matches(request.getCurrentPassword(), employee.getEmployeePassword())) {
            throw new PasswordMismatchException();
        }

        List<PasswordHistory> histories = passwordHistoryRepository
                .findByEmployeeIdOrderByPasswordChangedAtAsc(employee.getEmployeeId());

        boolean isDuplicate = histories.stream()
                .anyMatch(h -> passwordEncoder.matches(request.getNewPassword(), h.getPasswordHash()));

        if (isDuplicate) {
            throw new DuplicateFieldException("이전에 사용한 비밀번호는 사용할 수 없습니다");
        }

        if (histories.size() >= MAX_PASSWORD_HISTORY) {
            passwordHistoryRepository.delete(histories.get(0));
        }

        PasswordHistory newHistory = PasswordHistory.builder()
                .passwordHistoryId(idGenerator.generate())
                .employeeId(employee.getEmployeeId())
                .passwordHash(passwordEncoder.encode(request.getNewPassword()))
                .passwordChangedAt(LocalDateTime.now())
                .build();

        passwordHistoryRepository.save(newHistory);

        employee.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }
}
