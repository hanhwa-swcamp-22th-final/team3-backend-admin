package com.ohgiraffers.team3backendadmin.admin.command.application.service.auth;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.LoginRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.TokenResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.*;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.department.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeRole;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.AuthRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
                .employeeName("김관리")
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
        @DisplayName("정상 로그인 시 토큰을 반환한다")
        void loginSuccess() {
            // given
            LoginRequest request = new LoginRequest("admin@company.com", "rawPassword");

            given(aesEncryptor.encrypt("admin@company.com")).willReturn("encrypted-email");
            given(employeeRepository.findByEmployeeEmail("encrypted-email"))
                    .willReturn(Optional.of(employee));
            given(passwordEncoder.matches("rawPassword", "$2a$10$encodedPassword"))
                    .willReturn(true);
            given(departmentRepository.findById(1L))
                    .willReturn(Optional.of(department));
            given(jwtTokenProvider.createToken(
                    eq("EMP-0001"), eq("ADMIN"), eq("김관리"), eq("경영지원본부"), eq("시스템관리팀")))
                    .willReturn("access-token");
            given(jwtTokenProvider.createRefreshToken(
                    eq("EMP-0001"), eq("ADMIN"), eq("김관리"), eq("경영지원본부"), eq("시스템관리팀")))
                    .willReturn("refresh-token");
            given(jwtTokenProvider.getRefreshExpiration()).willReturn(604800000L);

            // when
            TokenResponse response = authCommandService.login(request);

            // then
            assertNotNull(response);
            assertEquals("access-token", response.getAccessToken());
            assertEquals("refresh-token", response.getRefreshToken());
            verify(jpaAuthRepository).save(any(RefreshToken.class));
        }

        @Test
        @DisplayName("존재하지 않는 이메일로 로그인 시 예외가 발생한다")
        void loginFailEmailNotFound() {
            // given
            LoginRequest request = new LoginRequest("unknown@company.com", "password");

            given(aesEncryptor.encrypt("unknown@company.com")).willReturn("encrypted-unknown");
            given(employeeRepository.findByEmployeeEmail("encrypted-unknown"))
                    .willReturn(Optional.empty());

            // when & then
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> authCommandService.login(request)
            );
            assertEquals("아이디 또는 비밀번호가 일치하지 않습니다", exception.getMessage());
        }

        @Test
        @DisplayName("사원이 휴가 중(ON_LEAVE)일 경우 로그인이 차단된다")
        void loginFailOnLeave() {
            // given
            LoginRequest request = new LoginRequest("admin@company.com", "rawPassword");
            Employee onLeaveEmployee = Employee.builder()
                    .employeeId(1L)
                    .employeeEmail("admin@company.com")
                    .employeeStatus(EmployeeStatus.ON_LEAVE)
                    .build();

            given(aesEncryptor.encrypt("admin@company.com")).willReturn("encrypted-email");
            given(employeeRepository.findByEmployeeEmail("encrypted-email"))
                    .willReturn(Optional.of(onLeaveEmployee));

            // when & then
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> authCommandService.login(request)
            );
            assertEquals("Employee is on leave", exception.getMessage());
        }

        @Test
        @DisplayName("비밀번호 불일치 시 예외가 발생한다")
        void loginFailPasswordMismatch() {
            // given
            LoginRequest request = new LoginRequest("admin@company.com", "wrongPassword");

            given(aesEncryptor.encrypt("admin@company.com")).willReturn("encrypted-email");
            given(employeeRepository.findByEmployeeEmail("encrypted-email"))
                    .willReturn(Optional.of(employee));
            given(passwordEncoder.matches("wrongPassword", "$2a$10$encodedPassword"))
                    .willReturn(false);

            // when & then
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> authCommandService.login(request)
            );
            assertEquals("아이디 또는 비밀번호가 일치하지 않습니다", exception.getMessage());
        }

        @Test
        @DisplayName("부서 정보가 없을 경우 예외가 발생한다")
        void loginFailDepartmentNotFound() {
            // given
            LoginRequest request = new LoginRequest("admin@company.com", "rawPassword");

            given(aesEncryptor.encrypt("admin@company.com")).willReturn("encrypted-email");
            given(employeeRepository.findByEmployeeEmail("encrypted-email"))
                    .willReturn(Optional.of(employee));
            given(passwordEncoder.matches("rawPassword", "$2a$10$encodedPassword"))
                    .willReturn(true);
            given(departmentRepository.findById(1L))
                    .willReturn(Optional.empty());

            // when & then
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> authCommandService.login(request)
            );
            assertEquals("부서 정보를 찾을 수 없습니다", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("logout 메서드")
    class Logout {

        @Test
        @DisplayName("정상 로그아웃 시 refresh token을 삭제한다")
        void logoutSuccess() {
            // given
            String refreshToken = "valid-refresh-token";

            given(jwtTokenProvider.validateToken(refreshToken)).willReturn(true);
            given(jwtTokenProvider.getEmployeeCodeFromJWT(refreshToken)).willReturn("EMP-0001");

            // when
            authCommandService.logout(refreshToken);

            // then
            verify(jpaAuthRepository).deleteById("EMP-0001");
        }
    }

    @Nested
    @DisplayName("refreshToken 메서드")
    class RefreshTokenTest {

        @Test
        @DisplayName("유효한 refresh token으로 새 토큰을 발급한다")
        void refreshTokenSuccess() {
            // given
            String provideRefreshToken = "valid-refresh-token";
            com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.RefreshToken storedToken =
                    com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.RefreshToken.builder()
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
                    eq("EMP-0001"), eq("ADMIN"), eq("김관리"), eq("경영지원본부"), eq("시스템관리팀")))
                    .willReturn("new-access-token");
            given(jwtTokenProvider.createRefreshToken(
                    eq("EMP-0001"), eq("ADMIN"), eq("김관리"), eq("경영지원본부"), eq("시스템관리팀")))
                    .willReturn("new-refresh-token");
            given(jwtTokenProvider.getRefreshExpiration()).willReturn(604800000L);

            // when
            TokenResponse response = authCommandService.refreshToken(provideRefreshToken);

            // then
            assertNotNull(response);
            assertEquals("new-access-token", response.getAccessToken());
            assertEquals("new-refresh-token", response.getRefreshToken());
            verify(jpaAuthRepository).save(any(com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.RefreshToken.class));
        }

        @Test
        @DisplayName("DB에 저장된 refresh token이 없으면 예외가 발생한다")
        void refreshTokenNotFoundInDB() {
            // given
            String provideRefreshToken = "valid-refresh-token";

            given(jwtTokenProvider.validateToken(provideRefreshToken)).willReturn(true);
            given(jwtTokenProvider.getEmployeeCodeFromJWT(provideRefreshToken)).willReturn("EMP-0001");
            given(jpaAuthRepository.findById("EMP-0001")).willReturn(Optional.empty());

            // when & then
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> authCommandService.refreshToken(provideRefreshToken)
            );
            assertEquals("해당 유저로 조회되는 refresh token 없음", exception.getMessage());
        }

        @Test
        @DisplayName("refresh token이 불일치하면 예외가 발생한다")
        void refreshTokenMismatch() {
            // given
            String provideRefreshToken = "provided-token";
            com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.RefreshToken storedToken =
                    com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.RefreshToken.builder()
                            .employeeCode("EMP-0001")
                            .token("different-stored-token")
                            .expiryDate(new Date(System.currentTimeMillis() + 604800000L))
                            .build();

            given(jwtTokenProvider.validateToken(provideRefreshToken)).willReturn(true);
            given(jwtTokenProvider.getEmployeeCodeFromJWT(provideRefreshToken)).willReturn("EMP-0001");
            given(jpaAuthRepository.findById("EMP-0001")).willReturn(Optional.of(storedToken));

            // when & then
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> authCommandService.refreshToken(provideRefreshToken)
            );
            assertEquals("refresh token이 일치하지 않음", exception.getMessage());
        }

        @Test
        @DisplayName("refresh token이 만료되었으면 예외가 발생한다")
        void refreshTokenExpired() {
            // given
            String provideRefreshToken = "expired-token";
            com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.RefreshToken storedToken =
                    com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.RefreshToken.builder()
                            .employeeCode("EMP-0001")
                            .token("expired-token")
                            .expiryDate(new Date(System.currentTimeMillis() - 1000))
                            .build();

            given(jwtTokenProvider.validateToken(provideRefreshToken)).willReturn(true);
            given(jwtTokenProvider.getEmployeeCodeFromJWT(provideRefreshToken)).willReturn("EMP-0001");
            given(jpaAuthRepository.findById("EMP-0001")).willReturn(Optional.of(storedToken));

            // when & then
            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> authCommandService.refreshToken(provideRefreshToken)
            );
            assertEquals("refresh token 기간 만료", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("buildTokenResponse 메서드")
    class BuildTokenResponse {

        @Test
        @DisplayName("토큰 응답에 accessToken과 refreshToken 쿠키가 포함된다")
        void buildTokenResponseSuccess() {
            // given
            TokenResponse tokenResponse = TokenResponse.builder()
                    .accessToken("test-access-token")
                    .refreshToken("test-refresh-token")
                    .build();

            // when
            ResponseEntity<ApiResponse<TokenResponse>> response =
                    authCommandService.buildTokenResponse(tokenResponse);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().getSuccess());
            assertEquals("test-access-token", response.getBody().getData().getAccessToken());

            String setCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            assertNotNull(setCookie);
            assertTrue(setCookie.contains("refreshToken=test-refresh-token"));
        }
    }

    @Nested
    @DisplayName("createRefreshTokenCookie 메서드")
    class CreateRefreshTokenCookie {

        @Test
        @DisplayName("refreshToken 쿠키가 올바른 속성으로 생성된다")
        void createRefreshTokenCookieSuccess() {
            // when
            ResponseCookie cookie = authCommandService.createRefreshTokenCookie("my-refresh-token");

            // then
            assertEquals("refreshToken", cookie.getName());
            assertEquals("my-refresh-token", cookie.getValue());
            assertTrue(cookie.isHttpOnly());
            assertEquals("/", cookie.getPath());
            assertEquals(Duration.ofDays(7), cookie.getMaxAge());
            assertEquals("Strict", cookie.getSameSite());
        }
    }

    @Nested
    @DisplayName("createDeleteRefreshTokenCookie 메서드")
    class CreateDeleteRefreshTokenCookie {

        @Test
        @DisplayName("삭제용 쿠키가 maxAge=0으로 생성된다")
        void createDeleteRefreshTokenCookieSuccess() {
            // when
            ResponseCookie cookie = authCommandService.createDeleteRefreshTokenCookie();

            // then
            assertEquals("refreshToken", cookie.getName());
            assertTrue(cookie.isHttpOnly());
            assertEquals("/", cookie.getPath());
            assertEquals(Duration.ZERO, cookie.getMaxAge());
            assertEquals("Strict", cookie.getSameSite());
        }
    }
}
