package com.hdp.common.security.web;

import com.hdp.common.security.dto.LoginRequest;
import com.hdp.common.security.dto.LoginResponse;
import com.hdp.common.security.dto.LogoutRequest;
import com.hdp.common.security.dto.RefreshRequest;
import com.hdp.common.security.dto.RefreshResponse;
import com.hdp.common.security.token.KeycloakTokenClient;
import com.hdp.common.security.token.TokenResponse;
import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.core.exception.BusinessException;
import com.hdp.core.exception.CoreErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login, refresh and logout proxied to Keycloak.
 *
 * <p>Path is relative to the service context-path, so customer-service exposes
 * {@code /api/v1/auth/*}. Login accepts email or phone as the Keycloak username.</p>
 */
@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final KeycloakTokenClient tokenClient;

    public AuthController(KeycloakTokenClient tokenClient) {
        this.tokenClient = tokenClient;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        String username = StringUtils.hasText(request.email()) ? request.email() : request.phone();
        if (!StringUtils.hasText(username) || !StringUtils.hasText(request.password())) {
            throw new BusinessException(CoreErrorCode.INVALID_CREDENTIALS);
        }

        TokenResponse token = tokenClient.login(username, request.password());
        return ApiResponse.success(new LoginResponse(
                token.accessToken(), token.refreshToken(), token.tokenType(), token.expiresIn()));
    }

    @PostMapping("/refresh")
    public ApiResponse<RefreshResponse> refresh(@RequestBody RefreshRequest request) {
        if (!StringUtils.hasText(request.refreshToken())) {
            throw new BusinessException(CoreErrorCode.INVALID_REFRESH_TOKEN);
        }

        TokenResponse token = tokenClient.refresh(request.refreshToken());
        return ApiResponse.success(new RefreshResponse(token.accessToken(), token.expiresIn()));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody(required = false) LogoutRequest request) {
        if (request != null && StringUtils.hasText(request.refreshToken())) {
            tokenClient.logout(request.refreshToken());
        }
        return ApiResponse.<Void>success(null);
    }
}