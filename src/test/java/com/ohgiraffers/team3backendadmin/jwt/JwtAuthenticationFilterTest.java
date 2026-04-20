package com.ohgiraffers.team3backendadmin.jwt;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.RefreshToken;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.AuthRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    private static final String TOKEN = "access-token";
    private static final String EMPLOYEE_CODE = "EMP-0001";

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthRepository authRepository;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        filter = new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService, authRepository);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("토큰과 DB의 loginSessionId가 일치하면 인증을 설정한다")
    void matchingLoginSessionIdSetsAuthentication() throws Exception {
        // given
        givenValidToken("session-id");
        given(authRepository.findById(EMPLOYEE_CODE)).willReturn(Optional.of(refreshToken("session-id")));
        given(userDetailsService.loadUserByUsername(EMPLOYEE_CODE)).willReturn(userDetails());

        // when
        filter.doFilter(requestWithBearerToken(), new MockHttpServletResponse(), new MockFilterChain());

        // then
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(EMPLOYEE_CODE, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    @DisplayName("토큰과 DB의 loginSessionId가 다르면 인증을 설정하지 않는다")
    void mismatchedLoginSessionIdDoesNotSetAuthentication() throws Exception {
        // given
        givenValidToken("old-session-id");
        given(authRepository.findById(EMPLOYEE_CODE)).willReturn(Optional.of(refreshToken("current-session-id")));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        filter.doFilter(requestWithBearerToken(), response, new MockFilterChain());

        // then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(401, response.getStatus());
        assertFalse(response.getContentAsString().contains("AUTH_003"));
        assertTrue(response.getContentAsString().contains("AUTH_005"));
    }

    @Test
    @DisplayName("DB에 refresh token이 없으면 인증을 설정하지 않는다")
    void missingRefreshTokenDoesNotSetAuthentication() throws Exception {
        // given
        givenValidToken("session-id");
        given(authRepository.findById(EMPLOYEE_CODE)).willReturn(Optional.empty());

        // when
        filter.doFilter(requestWithBearerToken(), new MockHttpServletResponse(), new MockFilterChain());

        // then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("기존 DB row처럼 loginSessionId가 없으면 인증을 허용한다")
    void nullDbLoginSessionIdAllowsAuthentication() throws Exception {
        // given
        givenValidToken("session-id");
        given(authRepository.findById(EMPLOYEE_CODE)).willReturn(Optional.of(refreshToken(null)));
        given(userDetailsService.loadUserByUsername(EMPLOYEE_CODE)).willReturn(userDetails());

        // when
        filter.doFilter(requestWithBearerToken(), new MockHttpServletResponse(), new MockFilterChain());

        // then
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("새 로그인 이후 기존 토큰처럼 loginSessionId가 없으면 인증을 설정하지 않는다")
    void nullTokenLoginSessionIdWithCurrentDbSessionDoesNotSetAuthentication() throws Exception {
        // given
        givenValidToken(null);
        given(authRepository.findById(EMPLOYEE_CODE)).willReturn(Optional.of(refreshToken("current-session-id")));
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        filter.doFilter(requestWithBearerToken(), response, new MockFilterChain());

        // then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("AUTH_005"));
    }

    private void givenValidToken(String loginSessionId) {
        given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);
        given(jwtTokenProvider.getEmployeeCodeFromJWT(TOKEN)).willReturn(EMPLOYEE_CODE);
        given(jwtTokenProvider.getLoginSessionIdFromJWT(TOKEN)).willReturn(loginSessionId);
    }

    private MockHttpServletRequest requestWithBearerToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + TOKEN);
        return request;
    }

    private RefreshToken refreshToken(String loginSessionId) {
        return RefreshToken.builder()
                .employeeCode(EMPLOYEE_CODE)
                .token("refresh-token")
                .loginSessionId(loginSessionId)
                .expiryDate(new Date(System.currentTimeMillis() + 604800000L))
                .build();
    }

    private UserDetails userDetails() {
        return User.withUsername(EMPLOYEE_CODE)
                .password("password")
                .authorities("ROLE_ADMIN")
                .build();
    }
}
