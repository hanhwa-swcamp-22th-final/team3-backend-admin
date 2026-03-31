package com.ohgiraffers.team3backendadmin.admin.command.application.service;


import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.LoginRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.TokenResponse;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Department;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.EmployeeStatus;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.RefreshToken;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.AuthRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.DepartmentRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.common.encryption.AesEncryptor;
import com.ohgiraffers.team3backendadmin.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthCommandService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepository jpaAuthRepository;
    private final AesEncryptor aesEncryptor;

    public TokenResponse login(LoginRequest loginRequest) {
        // 1. 이메일 암호화 후 조회
        Employee employee = this.employeeRepository.findByEmployeeEmail(aesEncryptor.encrypt(loginRequest.getEmployeeEmail()))
                .orElseThrow(() -> new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다"));

        // 2. 사원 상태 확인 (ON_LEAVE인 경우 로그인 차단)
        if (employee.getEmployeeStatus() == EmployeeStatus.ON_LEAVE) {
            throw new BadCredentialsException("Employee is on leave");
        }

        // 3. 비밀번호 매칭 확인
        if(!this.passwordEncoder.matches(loginRequest.getPassword(), employee.getEmployeePassword())) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다");
        }

        // 3. 부서 정보 조회
        Department department = this.departmentRepository.findById(employee.getDepartmentId())
                .orElseThrow(() -> new BadCredentialsException("부서 정보를 찾을 수 없습니다"));

        // 4. 비밀번호가 일치 -> 로그인 성공 -> 토큰 생성 -> 발급
        String accessToken = this.jwtTokenProvider.createToken(
                employee.getEmployeeCode(), employee.getEmployeeRole().name(),
                employee.getEmployeeName(), department.getDepartmentName(), department.getTeamName());
        String refreshToken = this.jwtTokenProvider.createRefreshToken(
                employee.getEmployeeCode(), employee.getEmployeeRole().name(),
                employee.getEmployeeName(), department.getDepartmentName(), department.getTeamName());

        // 5. refresh token DB에 저장(보안 및 토큰 재발급 검증용)
        RefreshToken tokenEntity = RefreshToken.builder()
                .employeeCode(employee.getEmployeeCode())
                .token(refreshToken)
                .expiryDate(new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshExpiration())).build();

        this.jpaAuthRepository.save(tokenEntity);

        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    /* DB refresh token 삭제 */
    public void logout(String refreshToken) {
        // refreshToken 검증 절차
        this.jwtTokenProvider.validateToken(refreshToken);

        String employeeCode = this.jwtTokenProvider.getEmployeeCodeFromJWT(refreshToken);

        this.jpaAuthRepository.deleteById(employeeCode); // DB에서 employee_code가 일치하는 행을 삭제
    }

    /* refresh token 검증 후 새 token 발급 서비스 */
    public TokenResponse refreshToken(String provideRefreshToken) {
        // refresh token 유효성 검사
        this.jwtTokenProvider.validateToken(provideRefreshToken);

        // 전달 받은 refresh token 에서 사원코드(employeeCode) 얻어오기
        String employeeCode = this.jwtTokenProvider.getEmployeeCodeFromJWT(provideRefreshToken);

        // DB에서 employee_code가 일치하는 행의 refresh token을 조회
        RefreshToken storedToken  = this.jpaAuthRepository.findById(employeeCode)
                .orElseThrow(()->new BadCredentialsException("해당 유저로 조회되는 refresh token 없음"));

        // 넘어온 요청 시 전달 받은 refresh token 과 DB에 저장된 refresh token이 일치하는지 확인
        if (!storedToken.getToken().equals(provideRefreshToken)) {
            throw new BadCredentialsException("refresh token이 일치하지 않음");
        }

        // DB에 저장된 token의 만료 기간이 현재 시간 보다 과거인지 확인
        if (storedToken.getExpiryDate().before(new Date())) {
            throw new BadCredentialsException("refresh token 기간 만료");
        }

        // employeeCode가 일치하는 회원(employee) 조회
        Employee employee = this.employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(()->new BadCredentialsException("해당 유저 없음"));

        // 부서 정보 조회
        Department department = this.departmentRepository.findById(employee.getDepartmentId())
                .orElseThrow(() -> new BadCredentialsException("부서 정보를 찾을 수 없습니다"));

        // 새로운 token 발급
        String accessToken = this.jwtTokenProvider.createToken(
                employee.getEmployeeCode(), employee.getEmployeeRole().name(),
                employee.getEmployeeName(), department.getDepartmentName(), department.getTeamName());
        String refreshToken = this.jwtTokenProvider.createRefreshToken(
                employee.getEmployeeCode(), employee.getEmployeeRole().name(),
                employee.getEmployeeName(), department.getDepartmentName(), department.getTeamName());

        // refresh token entity 생성 (저장용)
        RefreshToken tokenEntity = RefreshToken.builder()
                .employeeCode(employeeCode)
                .token(refreshToken)
                .expiryDate(new Date(System.currentTimeMillis() + this.jwtTokenProvider.getRefreshExpiration()))
                .build();
        // DB 저장 (PK 중복 행이 이미 존재 -> UPDATE)
        this.jpaAuthRepository.save(tokenEntity);

        // TokenResponse 반환
        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

}
