package com.hdp.common.security.token;

import com.hdp.common.security.config.HdpSecurityProperties;
import com.hdp.core.exception.BusinessException;
import com.hdp.core.exception.CoreErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.net.http.HttpClient;

/**
 * Talks to the Keycloak token endpoint. Access and refresh tokens are issued by Keycloak;
 * this client only proxies password and refresh_token grants.
 */
@Slf4j
public class KeycloakTokenClient {

    private final HdpSecurityProperties.Keycloak keycloak;
    private final RestClient restClient;

    public KeycloakTokenClient(HdpSecurityProperties properties) {
        this(properties, defaultRestClient(properties.keycloak()));
    }

    KeycloakTokenClient(HdpSecurityProperties properties, RestClient restClient) {
        this.keycloak = properties.keycloak();
        this.restClient = restClient;
    }

    public TokenResponse login(String username, String password) {
        MultiValueMap<String, String> form = baseForm();
        form.add("grant_type", "password");
        form.add("username", username);
        form.add("password", password);
        return requestToken(form, CoreErrorCode.INVALID_CREDENTIALS);
    }

    public TokenResponse refresh(String refreshToken) {
        MultiValueMap<String, String> form = baseForm();
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);
        return requestToken(form, CoreErrorCode.INVALID_REFRESH_TOKEN);
    }

    /**
     * Keycloak logout. Idempotent: an already invalidated token is treated as success.
     */
    public void logout(String refreshToken) {
        MultiValueMap<String, String> form = baseForm();
        form.add("refresh_token", refreshToken);
        try {
            restClient.post()
                    .uri(keycloak.logoutUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().is5xxServerError()) {
                throw new BusinessException(CoreErrorCode.INTERNAL_ERROR);
            }
            log.debug("Keycloak logout returned {}; token already invalidated", ex.getStatusCode().value());
        } catch (RestClientException ex) {
            log.error("Keycloak logout failed: {}", ex.getMessage());
            throw new BusinessException(CoreErrorCode.INTERNAL_ERROR);
        }
    }

    private TokenResponse requestToken(MultiValueMap<String, String> form, CoreErrorCode unauthorizedCode) {
        try {
            return restClient.post()
                    .uri(keycloak.tokenUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(TokenResponse.class);
        } catch (RestClientResponseException ex) {
            log.warn("Keycloak token request failed: status={} body={}",
                    ex.getStatusCode().value(), ex.getResponseBodyAsString());
            if (ex.getStatusCode().is4xxClientError()) {
                throw new BusinessException(unauthorizedCode);
            }
            throw new BusinessException(CoreErrorCode.INTERNAL_ERROR);
        } catch (RestClientException ex) {
            log.error("Keycloak token endpoint unreachable: {}", ex.getMessage());
            throw new BusinessException(CoreErrorCode.INTERNAL_ERROR);
        }
    }

    private MultiValueMap<String, String> baseForm() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", keycloak.clientId());
        if (StringUtils.hasText(keycloak.clientSecret())) {
            form.add("client_secret", keycloak.clientSecret());
        }
        if (StringUtils.hasText(keycloak.scope())) {
            form.add("scope", keycloak.scope());
        }
        return form;
    }

    private static RestClient defaultRestClient(HdpSecurityProperties.Keycloak keycloak) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(keycloak.connectTimeout())
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(keycloak.readTimeout());
        return RestClient.builder().requestFactory(requestFactory).build();
    }
}