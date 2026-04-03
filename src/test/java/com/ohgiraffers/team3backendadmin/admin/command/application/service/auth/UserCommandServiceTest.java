package com.ohgiraffers.team3backendadmin.admin.command.application.service.auth;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.PasswordChangeRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.ProfileUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeTier;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.passwordhistory.PasswordHistory;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.PasswordHistoryRepository;
import com.ohgiraffers.team3backendadmin.common.encryption.AesEncryptor;
import com.ohgiraffers.team3backendadmin.common.exception.DuplicateFieldException;
import com.ohgiraffers.team3backendadmin.common.exception.EmployeeNotFoundException;
import com.ohgiraffers.team3backendadmin.common.exception.PasswordMismatchException;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @InjectMocks
    private UserCommandService userCommandService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordHistoryRepository passwordHistoryRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private AesEncryptor aesEncryptor;

    @Nested
    @DisplayName("updateProfile 메서드")
    class UpdateProfile {

        private Employee loginUser;

        @BeforeEach
        void setUp() {
            loginUser = Employee.builder()
                    .employeeId(5000L)
                    .departmentId(100L)
                    .employeeCode("EMP2603001")
                    .employeeName("홍길동")
                    .employeeEmail("AES_hong@company.com")
                    .employeePhone("AES_010-1234-5678")
                    .employeeAddress("AES_서울시 강남구")
                    .employeeEmergencyContact("AES_010-9876-5432")
                    .employeeRole(EmployeeRole.WORKER)
                    .employeeStatus(EmployeeStatus.ACTIVE)
                    .employeeTier(EmployeeTier.B)
                    .build();
        }

        @Test
        @DisplayName("로그인한 사원 본인의 모든 필드가 정상적으로 수정된다")
        void updateProfileAllFields() {
            // given
            ProfileUpdateRequest request = new ProfileUpdateRequest(
                    "김철수", "kim@company.com",
                    "010-9999-8888", "부산시 해운대구", "010-1111-2222"
            );

            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(loginUser));
            given(aesEncryptor.encrypt(anyString()))
                    .willAnswer(invocation -> "AES_" + invocation.getArgument(0));
            given(employeeRepository.existsByEmployeeEmail("AES_kim@company.com"))
                    .willReturn(false);
            given(employeeRepository.existsByEmployeePhone("AES_010-9999-8888"))
                    .willReturn(false);

            // when
            userCommandService.updateProfile(request, "EMP2603001");

            // then
            assertEquals("김철수", loginUser.getEmployeeName());
            assertEquals("AES_kim@company.com", loginUser.getEmployeeEmail());
            assertEquals("AES_010-9999-8888", loginUser.getEmployeePhone());
            assertEquals("AES_부산시 해운대구", loginUser.getEmployeeAddress());
            assertEquals("AES_010-1111-2222", loginUser.getEmployeeEmergencyContact());
        }

        @Test
        @DisplayName("이름과 이메일만 수정하면 나머지 필드는 변경되지 않는다")
        void updateProfilePartialFields() {
            // given
            ProfileUpdateRequest request = new ProfileUpdateRequest(
                    "김철수", "kim@company.com",
                    null, null, null
            );

            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(loginUser));
            given(aesEncryptor.encrypt(anyString()))
                    .willAnswer(invocation -> "AES_" + invocation.getArgument(0));
            given(employeeRepository.existsByEmployeeEmail("AES_kim@company.com"))
                    .willReturn(false);

            // when
            userCommandService.updateProfile(request, "EMP2603001");

            // then
            assertEquals("김철수", loginUser.getEmployeeName());
            assertEquals("AES_kim@company.com", loginUser.getEmployeeEmail());
            assertEquals("AES_010-1234-5678", loginUser.getEmployeePhone());
            assertEquals("AES_서울시 강남구", loginUser.getEmployeeAddress());
            assertEquals("AES_010-9876-5432", loginUser.getEmployeeEmergencyContact());
        }

        @Test
        @DisplayName("본인의 기존 이메일과 동일하면 중복 예외가 발생한다")
        void updateProfileSameEmailThrowsException() {
            // given
            ProfileUpdateRequest request = new ProfileUpdateRequest(
                    null, "hong@company.com",
                    null, null, null
            );

            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(loginUser));
            given(aesEncryptor.encrypt("hong@company.com"))
                    .willReturn("AES_hong@company.com");
            given(employeeRepository.existsByEmployeeEmail("AES_hong@company.com"))
                    .willReturn(true);

            // when & then
            DuplicateFieldException exception = assertThrows(
                    DuplicateFieldException.class,
                    () -> userCommandService.updateProfile(request, "EMP2603001")
            );
            assertEquals("이미 사용중인 이메일 입니다", exception.getMessage());
        }

        @Test
        @DisplayName("다른 사원이 사용 중인 이메일이면 예외가 발생한다")
        void updateProfileDuplicateEmail() {
            // given
            ProfileUpdateRequest request = new ProfileUpdateRequest(
                    null, "taken@company.com",
                    null, null, null
            );

            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(loginUser));
            given(aesEncryptor.encrypt("taken@company.com"))
                    .willReturn("AES_taken@company.com");
            given(employeeRepository.existsByEmployeeEmail("AES_taken@company.com"))
                    .willReturn(true);

            // when & then
            DuplicateFieldException exception = assertThrows(
                    DuplicateFieldException.class,
                    () -> userCommandService.updateProfile(request, "EMP2603001")
            );
            assertEquals("이미 사용중인 이메일 입니다", exception.getMessage());
        }

        @Test
        @DisplayName("다른 사원이 사용 중인 전화번호이면 예외가 발생한다")
        void updateProfileDuplicatePhone() {
            // given
            ProfileUpdateRequest request = new ProfileUpdateRequest(
                    null, null,
                    "010-0000-0000", null, null
            );

            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(loginUser));
            given(aesEncryptor.encrypt("010-0000-0000"))
                    .willReturn("AES_010-0000-0000");
            given(employeeRepository.existsByEmployeePhone("AES_010-0000-0000"))
                    .willReturn(true);

            // when & then
            DuplicateFieldException exception = assertThrows(
                    DuplicateFieldException.class,
                    () -> userCommandService.updateProfile(request, "EMP2603001")
            );
            assertEquals("이미 사용중인 전화번호 입니다", exception.getMessage());
        }

        @Test
        @DisplayName("본인의 기존 전화번호와 동일하면 중복 예외가 발생한다")
        void updateProfileSamePhoneThrowsException() {
            // given
            ProfileUpdateRequest request = new ProfileUpdateRequest(
                    null, null,
                    "010-1234-5678", null, null
            );

            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(loginUser));
            given(aesEncryptor.encrypt("010-1234-5678"))
                    .willReturn("AES_010-1234-5678");
            given(employeeRepository.existsByEmployeePhone("AES_010-1234-5678"))
                    .willReturn(true);

            // when & then
            DuplicateFieldException exception = assertThrows(
                    DuplicateFieldException.class,
                    () -> userCommandService.updateProfile(request, "EMP2603001")
            );
            assertEquals("이미 사용중인 전화번호 입니다", exception.getMessage());
        }

        @Test
        @DisplayName("로그인한 사원코드가 존재하지 않으면 예외가 발생한다")
        void updateProfileEmployeeNotFound() {
            // given
            ProfileUpdateRequest request = new ProfileUpdateRequest(
                    "김철수", null,
                    null, null, null
            );

            given(employeeRepository.findByEmployeeCode("UNKNOWN"))
                    .willReturn(Optional.empty());

            // when & then
            EmployeeNotFoundException exception = assertThrows(
                    EmployeeNotFoundException.class,
                    () -> userCommandService.updateProfile(request, "UNKNOWN")
            );
            assertEquals("해당 사원을 찾을 수 없습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("changePassword 메서드")
    class ChangePassword {

        private Employee loginUser;

        @BeforeEach
        void setUp() {
            loginUser = Employee.builder()
                    .employeeId(5000L)
                    .departmentId(100L)
                    .employeeCode("EMP2603001")
                    .employeeName("홍길동")
                    .employeePassword("$2a$10$encodedCurrentPassword")
                    .employeeRole(EmployeeRole.WORKER)
                    .employeeStatus(EmployeeStatus.ACTIVE)
                    .employeeTier(EmployeeTier.B)
                    .build();
        }

        @Test
        @DisplayName("이력이 3개 미만이면 새 이력이 추가된다")
        void changePasswordUnderLimit() {
            // given
            PasswordChangeRequest request = new PasswordChangeRequest("currentPw", "newPw123");

            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(loginUser));
            given(passwordEncoder.matches("currentPw", "$2a$10$encodedCurrentPassword"))
                    .willReturn(true);
            given(passwordEncoder.matches("newPw123", "$2a$10$encodedCurrentPassword"))
                    .willReturn(false);
            given(passwordHistoryRepository.findByEmployeeIdOrderByPasswordChangedAtAsc(5000L))
                    .willReturn(Collections.emptyList());
            given(idGenerator.generate()).willReturn(9000L);
            given(passwordEncoder.encode("newPw123")).willReturn("$2a$10$encodedNewPw");

            // when
            userCommandService.changePassword(request, "EMP2603001");

            // then
            ArgumentCaptor<PasswordHistory> captor = ArgumentCaptor.forClass(PasswordHistory.class);
            verify(passwordHistoryRepository).save(captor.capture());

            PasswordHistory saved = captor.getValue();
            assertEquals(9000L, saved.getPasswordHistoryId());
            assertEquals(5000L, saved.getEmployeeId());
            assertEquals("$2a$10$encodedNewPw", saved.getPasswordHash());

            assertEquals("$2a$10$encodedNewPw", loginUser.getEmployeePassword());
        }

        @Test
        @DisplayName("이력이 3개 이상이면 가장 오래된 이력이 삭제되고 새 이력이 추가된다")
        void changePasswordOverLimit() {
            // given
            PasswordChangeRequest request = new PasswordChangeRequest("currentPw", "newPw123");

            PasswordHistory oldest = PasswordHistory.builder()
                    .passwordHistoryId(1000L).employeeId(5000L)
                    .passwordHash("$2a$10$old1").passwordChangedAt(LocalDateTime.of(2026, 1, 1, 0, 0))
                    .build();
            PasswordHistory middle = PasswordHistory.builder()
                    .passwordHistoryId(2000L).employeeId(5000L)
                    .passwordHash("$2a$10$old2").passwordChangedAt(LocalDateTime.of(2026, 2, 1, 0, 0))
                    .build();
            PasswordHistory newest = PasswordHistory.builder()
                    .passwordHistoryId(3000L).employeeId(5000L)
                    .passwordHash("$2a$10$old3").passwordChangedAt(LocalDateTime.of(2026, 3, 1, 0, 0))
                    .build();

            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(loginUser));
            given(passwordEncoder.matches("currentPw", "$2a$10$encodedCurrentPassword"))
                    .willReturn(true);
            given(passwordEncoder.matches("newPw123", "$2a$10$encodedCurrentPassword"))
                    .willReturn(false);
            given(passwordHistoryRepository.findByEmployeeIdOrderByPasswordChangedAtAsc(5000L))
                    .willReturn(List.of(oldest, middle, newest));
            given(passwordEncoder.matches("newPw123", "$2a$10$old1")).willReturn(false);
            given(passwordEncoder.matches("newPw123", "$2a$10$old2")).willReturn(false);
            given(passwordEncoder.matches("newPw123", "$2a$10$old3")).willReturn(false);
            given(idGenerator.generate()).willReturn(9000L);
            given(passwordEncoder.encode("newPw123")).willReturn("$2a$10$encodedNewPw");

            // when
            userCommandService.changePassword(request, "EMP2603001");

            // then
            verify(passwordHistoryRepository).delete(oldest);

            ArgumentCaptor<PasswordHistory> captor = ArgumentCaptor.forClass(PasswordHistory.class);
            verify(passwordHistoryRepository).save(captor.capture());
            assertEquals("$2a$10$encodedNewPw", captor.getValue().getPasswordHash());

            assertEquals("$2a$10$encodedNewPw", loginUser.getEmployeePassword());
        }

        @Test
        @DisplayName("이전에 사용한 비밀번호이면 예외가 발생한다")
        void changePasswordDuplicate() {
            // given
            PasswordChangeRequest request = new PasswordChangeRequest("currentPw", "oldPw");

            PasswordHistory history = PasswordHistory.builder()
                    .passwordHistoryId(1000L).employeeId(5000L)
                    .passwordHash("$2a$10$encodedOldPw").passwordChangedAt(LocalDateTime.of(2026, 1, 1, 0, 0))
                    .build();

            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(loginUser));
            given(passwordEncoder.matches("currentPw", "$2a$10$encodedCurrentPassword"))
                    .willReturn(true);
            given(passwordEncoder.matches("oldPw", "$2a$10$encodedCurrentPassword"))
                    .willReturn(false);
            given(passwordHistoryRepository.findByEmployeeIdOrderByPasswordChangedAtAsc(5000L))
                    .willReturn(List.of(history));
            given(passwordEncoder.matches("oldPw", "$2a$10$encodedOldPw"))
                    .willReturn(true);

            // when & then
            DuplicateFieldException exception = assertThrows(
                    DuplicateFieldException.class,
                    () -> userCommandService.changePassword(request, "EMP2603001")
            );
            assertEquals("이전에 사용한 비밀번호는 사용할 수 없습니다", exception.getMessage());
        }

        @Test
        @DisplayName("새 비밀번호가 현재 비밀번호와 동일하면 예외가 발생한다")
        void changePasswordSameAsCurrent() {
            // given
            PasswordChangeRequest request = new PasswordChangeRequest("currentPw", "currentPw");

            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(loginUser));
            given(passwordEncoder.matches("currentPw", "$2a$10$encodedCurrentPassword"))
                    .willReturn(true);

            // when & then
            DuplicateFieldException exception = assertThrows(
                    DuplicateFieldException.class,
                    () -> userCommandService.changePassword(request, "EMP2603001")
            );
            assertEquals("새 비밀번호는 현재 비밀번호와 같을 수 없습니다", exception.getMessage());
        }

        @Test
        @DisplayName("현재 비밀번호가 일치하지 않으면 예외가 발생한다")
        void changePasswordWrongCurrent() {
            // given
            PasswordChangeRequest request = new PasswordChangeRequest("wrongPw", "newPw123");

            given(employeeRepository.findByEmployeeCode("EMP2603001"))
                    .willReturn(Optional.of(loginUser));
            given(passwordEncoder.matches("wrongPw", "$2a$10$encodedCurrentPassword"))
                    .willReturn(false);

            // when & then
            PasswordMismatchException exception = assertThrows(
                    PasswordMismatchException.class,
                    () -> userCommandService.changePassword(request, "EMP2603001")
            );
            assertEquals("현재 비밀번호가 일치하지 않습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("사원코드가 존재하지 않으면 예외가 발생한다")
        void changePasswordEmployeeNotFound() {
            // given
            PasswordChangeRequest request = new PasswordChangeRequest("currentPw", "newPw123");

            given(employeeRepository.findByEmployeeCode("UNKNOWN"))
                    .willReturn(Optional.empty());

            // when & then
            EmployeeNotFoundException exception = assertThrows(
                    EmployeeNotFoundException.class,
                    () -> userCommandService.changePassword(request, "UNKNOWN")
            );
            assertEquals("해당 사원을 찾을 수 없습니다.", exception.getMessage());
        }
    }
}
