package com.ohgiraffers.team3backendadmin.admin.command.application.service.auth;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.LoginRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.TokenResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.RefreshToken;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.AuthRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.common.encryption.AesEncryptor;
import com.ohgiraffers.team3backendadmin.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthCommandServiceTest {

    @InjectMocks
    private AuthCommandService authCommandService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthRepository jpaAuthRepository;

    @Mock
    private AesEncryptor aesEncryptor;

    private Employee employee;
    private Department department;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
            .employeeId(1L)
            .departmentId(1L)
            .employeeCode("EMP-0001")
            .employeeName("관리자")
            .employeeEmail("admin@company.com")
            .employeePassword("$2a$10$encodedPassword")
            .employeeRole(EmployeeRole.ADMIN)
            .employeeStatus(EmployeeStatus.ACTIVE)
            .build();

        department = Department.builder()
            .departmentId(1L)
            .parentDepartmentId(1L)
            .departmentName("경영지원본부")
            .teamName("시스템관리팀")
            .depth("Root")
            .build();
    }

    @Nested
    @DisplayName("login 메서드")
    class Login {

        @Test
        @DisplayName("로그인 성공 시 access token과 refresh token을 반환한다")
        void loginSuccess() {
            LoginRequest request = new LoginRequest("admin@company.com", "rawPassword");

            given(aesEncryptor.encrypt("admin@company.com")).willReturn("encrypted-email");
            given(employeeRepository.findByEmployeeEmail("encrypted-email")).willReturn(Optional.of(employee));
            given(passwordEncoder.matches("rawPassword", "$2a$10$encodedPassword")).willReturn(true);
            given(departmentRepository.findById(1L)).willReturn(Optional.of(department));
            given(jwtTokenProvider.createToken(
                eq("EMP-0001"), eq("ADMIN"), eq("관리자"), eq("경영지원본부"), eq("시스템관리팀")
            )).willReturn("access-token");
            given(jwtTokenProvider.createRefreshToken(
                eq("EMP-0001"), eq("ADMIN"), eq("관리자"), eq("경영지원본부"), eq("시스템관리팀")
            )).willReturn("refresh-token");
            given(jwtTokenProvider.getRefreshExpiration()).willReturn(604800000L);

            TokenResponse response = authCommandService.login(request);

            assertNotNull(response);
            assertEquals("access-token", response.getAccessToken());
            assertEquals("refresh-token", response.getRefreshToken());
            verify(jpaAuthRepository).save(any(RefreshToken.class));
        }

        @Test
        @DisplayName("이메일이 존재하지 않으면 예외를 반환한다")
        void loginFailEmailNotFound() {
            LoginRequest request = new LoginRequest("unknown@company.com", "password");

            given(aesEncryptor.encrypt("unknown@company.com")).willReturn("encrypted-unknown");
            given(employeeRepository.findByEmployeeEmail("encrypted-unknown")).willReturn(Optional.empty());

            BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authCommandService.login(request)
            );
            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("휴직 상태 직원이면 로그인 예외를 반환한다")
        void loginFailOnLeave() {
            LoginRequest request = new LoginRequest("admin@company.com", "rawPassword");
            Employee onLeaveEmployee = Employee.builder()
                .employeeId(1L)
                .employeeEmail("admin@company.com")
                .employeeStatus(EmployeeStatus.ON_LEAVE)
                .build();

            given(aesEncryptor.encrypt("admin@company.com")).willReturn("encrypted-email");
            given(employeeRepository.findByEmployeeEmail("encrypted-email")).willReturn(Optional.of(onLeaveEmployee));

            BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authCommandService.login(request)
            );
            assertEquals("Employee is on leave", exception.getMessage());
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 예외를 반환한다")
        void loginFailPasswordMismatch() {
            LoginRequest request = new LoginRequest("admin@company.com", "wrongPassword");

            given(aesEncryptor.encrypt("admin@company.com")).willReturn("encrypted-email");
            given(employeeRepository.findByEmployeeEmail("encrypted-email")).willReturn(Optional.of(employee));
            given(passwordEncoder.matches("wrongPassword", "$2a$10$encodedPassword")).willReturn(false);

            BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authCommandService.login(request)
            );
            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("부서 정보가 없으면 예외를 반환한다")
        void loginFailDepartmentNotFound() {
            LoginRequest request = new LoginRequest("admin@company.com", "rawPassword");

            given(aesEncryptor.encrypt("admin@company.com")).willReturn("encrypted-email");
            given(employeeRepository.findByEmployeeEmail("encrypted-email")).willReturn(Optional.of(employee));
            given(passwordEncoder.matches("rawPassword", "$2a$10$encodedPassword")).willReturn(true);
            given(departmentRepository.findById(1L)).willReturn(Optional.empty());

            BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authCommandService.login(request)
            );
            assertNotNull(exception.getMessage());
        }
    }

    @Nested
    @DisplayName("logout 메서드")
    class Logout {

        @Test
        @DisplayName("로그아웃 성공 시 refresh token을 삭제한다")
        void logoutSuccess() {
            String refreshToken = "valid-refresh-token";

            given(jwtTokenProvider.validateToken(refreshToken)).willReturn(true);
            given(jwtTokenProvider.getEmployeeCodeFromJWT(refreshToken)).willReturn("EMP-0001");

            authCommandService.logout(refreshToken);

            verify(jpaAuthRepository).deleteById("EMP-0001");
        }
    }

    @Nested
    @DisplayName("refreshToken 메서드")
    class RefreshTokenTest {

        @Test
        @DisplayName("유효한 refresh token이면 새 토큰을 발급한다")
        void refreshTokenSuccess() {
            String provideRefreshToken = "valid-refresh-token";
            RefreshToken storedToken = RefreshToken.builder()
                .employeeCode("EMP-0001")
                .token("valid-refresh-token")
                .expiryDate(new Date(System.currentTimeMillis() + 604800000L))
                .build();

            given(jwtTokenProvider.validateToken(provideRefreshToken)).willReturn(true);
            given(jwtTokenProvider.getEmployeeCodeFromJWT(provideRefreshToken)).willReturn("EMP-0001");
            given(jpaAuthRepository.findById("EMP-0001")).willReturn(Optional.of(storedToken));
            given(employeeRepository.findByEmployeeCode("EMP-0001")).willReturn(Optional.of(employee));
            given(departmentRepository.findById(1L)).willReturn(Optional.of(department));
            given(jwtTokenProvider.createToken(
                eq("EMP-0001"), eq("ADMIN"), eq("관리자"), eq("경영지원본부"), eq("시스템관리팀")
            )).willReturn("new-access-token");
            given(jwtTokenProvider.createRefreshToken(
                eq("EMP-0001"), eq("ADMIN"), eq("관리자"), eq("경영지원본부"), eq("시스템관리팀")
            )).willReturn("new-refresh-token");
            given(jwtTokenProvider.getRefreshExpiration()).willReturn(604800000L);

            TokenResponse response = authCommandService.refreshToken(provideRefreshToken);

            assertNotNull(response);
            assertEquals("new-access-token", response.getAccessToken());
            assertEquals("new-refresh-token", response.getRefreshToken());
            verify(jpaAuthRepository).save(any(RefreshToken.class));
        }

        @Test
        @DisplayName("DB에 refresh token이 없으면 예외를 반환한다")
        void refreshTokenNotFoundInDB() {
            String provideRefreshToken = "valid-refresh-token";

            given(jwtTokenProvider.validateToken(provideRefreshToken)).willReturn(true);
            given(jwtTokenProvider.getEmployeeCodeFromJWT(provideRefreshToken)).willReturn("EMP-0001");
            given(jpaAuthRepository.findById("EMP-0001")).willReturn(Optional.empty());

            BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authCommandService.refreshToken(provideRefreshToken)
            );
            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("refresh token이 일치하지 않으면 예외를 반환한다")
        void refreshTokenMismatch() {
            String provideRefreshToken = "provided-token";
            RefreshToken storedToken = RefreshToken.builder()
                .employeeCode("EMP-0001")
                .token("different-stored-token")
                .expiryDate(new Date(System.currentTimeMillis() + 604800000L))
                .build();

            given(jwtTokenProvider.validateToken(provideRefreshToken)).willReturn(true);
            given(jwtTokenProvider.getEmployeeCodeFromJWT(provideRefreshToken)).willReturn("EMP-0001");
            given(jpaAuthRepository.findById("EMP-0001")).willReturn(Optional.of(storedToken));

            BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authCommandService.refreshToken(provideRefreshToken)
            );
            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("refresh token이 만료되면 예외를 반환한다")
        void refreshTokenExpired() {
            String provideRefreshToken = "expired-token";
            RefreshToken storedToken = RefreshToken.builder()
                .employeeCode("EMP-0001")
                .token("expired-token")
                .expiryDate(new Date(System.currentTimeMillis() - 1000))
                .build();

            given(jwtTokenProvider.validateToken(provideRefreshToken)).willReturn(true);
            given(jwtTokenProvider.getEmployeeCodeFromJWT(provideRefreshToken)).willReturn("EMP-0001");
            given(jpaAuthRepository.findById("EMP-0001")).willReturn(Optional.of(storedToken));

            BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authCommandService.refreshToken(provideRefreshToken)
            );
            assertNotNull(exception.getMessage());
        }
    }
}