package com.ohgiraffers.team3backendadmin.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import com.ohgiraffers.team3backendadmin.common.exception.ErrorCode;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.AuthRepository;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.RefreshToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final AuthRepository authRepository;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Request Header에서 JWT 토큰 추출
        String token = getJwtFromRequest(request);

        // 2. 토큰이 존재하면 유효성 검사 및 인증 설정
        if (StringUtils.hasText(token)) {
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    // 3. 토큰에서 사원코드 추출
                    String employeeCode = jwtTokenProvider.getEmployeeCodeFromJWT(token);
                    String tokenSessionId = jwtTokenProvider.getLoginSessionIdFromJWT(token);

                    // 4. 로그아웃 여부 및 최신 로그인 세션 여부 확인
                    Optional<RefreshToken> storedTokenOpt = authRepository.findById(employeeCode);
                    if (storedTokenOpt.isEmpty()) {
                        log.warn("로그아웃된 사용자의 access token 요청: {}", employeeCode);
                    } else if (isSupersededSession(tokenSessionId, storedTokenOpt.get().getLoginSessionId())) {
                        log.warn("다른 기기에서 로그인하여 세션이 만료됨: {}", employeeCode);
                        writeUnauthorizedResponse(response, ErrorCode.SESSION_SUPERSEDED);
                        return;
                    } else {
                        // 5. 사원코드로 사용자 정보(UserDetails) 로드
                        UserDetails userDetails = userDetailsService.loadUserByUsername(employeeCode);

                        // 6. Authentication 객체 생성 (권한 정보 포함)
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );

                        // 7. SecurityContextHolder에 Authentication 객체 저장
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                // 토큰이 유효하지 않은 경우 인증을 설정하지 않음
                // Spring Security의 AuthorizationFilter가 인증 실패를 처리
                log.warn("JWT 인증 실패: {}", e.getMessage());
            }
        }

        /* SecurityContextHolder의 Authentication이 설정되지 않은 경우
         * 이어지는 필터(AuthorizationFilter)에서 접근이 거부된다. */
        filterChain.doFilter(request, response);
    }

    private boolean isSupersededSession(String tokenSessionId, String dbSessionId) {
        return dbSessionId != null && !dbSessionId.equals(tokenSessionId);
    }

    private void writeUnauthorizedResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<Void> apiResponse = ApiResponse.failure(
                errorCode.getCode(),
                errorCode.getMessage()
        );
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
