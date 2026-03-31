package com.ohgiraffers.team3backendadmin.admin.command.application.controller;


import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.LoginRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.PasswordChangeRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.ProfileUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.TokenResponse;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.auth.AuthCommandService;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.auth.UserCommandService;
import com.ohgiraffers.team3backendadmin.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthCommandController {

    private final AuthCommandService authCommandService;
    private final UserCommandService userCommandService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = this.authCommandService.login(loginRequest);
        return authCommandService.buildTokenResponse(tokenResponse);
    }

    /* refresh token을 요청 시 전달 받아 인증된 token 이면 새 access/refresh token을 발급해서 반환 */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        // 쿠키에 refresh token이 없을 경우 -> 로그인 이전
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // refresh token이 문제가 없다면 새로운 access, refresh token 발급 후 반환
        TokenResponse tokenResponse = this.authCommandService.refreshToken(refreshToken);
        return authCommandService.buildTokenResponse(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        // refresh token이 존재할 경우 -> login 상태
        if (refreshToken != null) {
            this.authCommandService.logout(refreshToken); // DB refresh token 삭제
        }

        ResponseCookie deleteCookie = authCommandService.createDeleteRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(ApiResponse.success(null));
    }

    /**
     * 로그인한 사원 본인의 개인정보를 수정하는 Api
     * @param request ProfileUpdateRequest
     * @param userDetails Login User의 권한 정보를 담고있는 객체
     * @return ResponseEntity<ApiResponse<Void>>
     */
    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @Valid @RequestBody ProfileUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        userCommandService.updateProfile(request, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody PasswordChangeRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        userCommandService.changePassword(request, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
