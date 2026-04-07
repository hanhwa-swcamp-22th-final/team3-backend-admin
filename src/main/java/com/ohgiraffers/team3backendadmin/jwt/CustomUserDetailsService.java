package com.ohgiraffers.team3backendadmin.jwt;


import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.employee.Employee;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.EmployeeRepository;
import com.ohgiraffers.team3backendadmin.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    /* UserDetails : Spring Security가 관리하는 사용자 정보 객체 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1) DB에서 employeeCode가 일치하는 회원 조회
        Employee employee = this.employeeRepository.findByEmployeeCode(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다"));

        // 2) CustomUserDetails를 만들어서 반환 (employeeId 포함 → JPA Auditing에서 사용)
        return new CustomUserDetails(
                employee.getEmployeeId(),
                employee.getEmployeeCode(),
                Collections.singleton(new SimpleGrantedAuthority(employee.getEmployeeRole().name()))
        );
    }

}
