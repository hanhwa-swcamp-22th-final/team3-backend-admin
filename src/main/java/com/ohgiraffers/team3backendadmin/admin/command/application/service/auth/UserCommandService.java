package com.ohgiraffers.team3backendadmin.admin.command.application.service.auth;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.ProfileUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.common.encryption.AesEncryptor;
import com.ohgiraffers.team3backendadmin.common.exception.DuplicateFieldException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final EmployeeRepository employeeRepository;
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
}
