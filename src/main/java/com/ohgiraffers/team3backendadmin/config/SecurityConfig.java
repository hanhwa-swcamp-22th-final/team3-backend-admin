package com.ohgiraffers.team3backendadmin.config;



import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.AuthRepository;
import com.ohgiraffers.team3backendadmin.jwt.JwtAuthenticationFilter;
import com.ohgiraffers.team3backendadmin.jwt.JwtTokenProvider;
import com.ohgiraffers.team3backendadmin.jwt.RestAccessDeniedHandler;
import com.ohgiraffers.team3backendadmin.jwt.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize, @PostAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final AuthRepository authRepository;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /* Spring Security와 연결된 설정 객체 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF 비활성화: 모든 인증은 Authorization 헤더(Bearer JWT) 또는
        // SameSite=Strict HttpOnly 쿠키(refreshToken)로 처리되므로 CSRF 공격 벡터 없음
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                // 세션 로그인X -> 토큰 로그인 설정 O
                // 세션을 생성하지 않고, SecurityContextHolder에서 세션 저장 X
                // 모든 요청에 독립적, 인증 정보는 클라이언트 요청 시 전달된 토큰에 의지함
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 인증, 인가 실패 핸들러 추가
                .exceptionHandling(exception
                        -> exception.authenticationEntryPoint(this.restAuthenticationEntryPoint)
                                    .accessDeniedHandler(this.restAccessDeniedHandler)
                )
                // 요청 http method, url 기준으로 인증, 인가 필요 여부를 설정
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/auth/login",
                                        "/api/v1/auth/refresh",
                                        "/api/v1/auth/logout"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/auth/test").permitAll()
                                // DB삽입 오류로 인한 401에러 차단
                                .requestMatchers("/error").permitAll()
                                // 서버 간 내부 호출 — 알고리즘 버전 조회, 도메인 키워드 조회
                                .requestMatchers(HttpMethod.GET, "/api/v1/algorithm-version", "/api/v1/algorithm-version/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/domain-keyword", "/api/v1/domain-keyword/**").permitAll()
                                // swagger api
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/swagger-resources/**",
                                        "/webjars/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                // UsernamePasswordAuthenticationFilter 앞에 jwtAuthenticationFilter 필터를 추가
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService, authRepository),
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

}
