package com.ohgiraffers.team3backendadmin.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long employeeId;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return null;
    }
}
