package com.ohgiraffers.team3backendadmin.auth.command.application.service;


import com.ohgiraffers.team3backendadmin.auth.command.domain.aggregate.Employee;
import com.ohgiraffers.team3backendadmin.auth.command.domain.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
        // 1) DB에서 employeeEmail이 일치하는 회원 조회
        Employee employee = this.employeeRepository.findByEmployeeEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다"));

        // 2) UserDetails Interface를 구현한 객체를 만들어서 반환
        return new User(
                employee.getEmployeeCode(),
                employee.getEmployeePassword(),
                Collections.singleton(new SimpleGrantedAuthority(employee.getEmployeeRole().name()))
        );
    }

}
