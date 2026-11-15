package com.hdp.common.security.token;

import com.hdp.common.security.config.HdpSecurityProperties;
import com.hdp.core.exception.BusinessException;
import com.hdp.core.exception.CoreErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class KeycloakTokenClientTest {

    private static final String TOKEN_URL = "http://localhost:8282/realms/hdp/protocol/openid-connect/token";
    private static final String LOGOUT_URL = "http://localhost:8282/realms/hdp/protocol/openid-connect/logout";

    private final HdpSecurityProperties properties = new HdpSecurityProperties(
            true, "http://localhost:8282/realms/hdp", null, null, null, null,
            new HdpSecurityProperties.Keycloak(
                    "http://localhost:8282", "hdp", "hdp-backend", "secret", null, null, "openid"),
            null);

    private MockRestServiceServer server;
    private KeycloakTokenClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        client = new KeycloakTokenClient(properties, builder.build());
    }

    @Test
    void loginPostsPasswordGrantAndMapsTokens() {
        server.expect(requestTo(TOKEN_URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("grant_type=password")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("username=buyer%40hdp.com")))
                .andRespond(withSuccess("""
                        {"access_token":"access","refresh_token":"refresh","token_type":"Bearer","expires_in":3600}
                        """, MediaType.APPLICATION_JSON));

        TokenResponse token = client.login("buyer@hdp.com", "Buyer@123");

        assertThat(token.accessToken()).isEqualTo("access");
        assertThat(token.refreshToken()).isEqualTo("refresh");
        assertThat(token.tokenType()).isEqualTo("Bearer");
        assertThat(token.expiresIn()).isEqualTo(3600);
        server.verify();
    }

    @Test
    void loginInvalidGrantThrowsInvalidCredentials() {
        server.expect(requestTo(TOKEN_URL))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\":\"invalid_grant\"}"));

        assertThatThrownBy(() -> client.login("buyer@hdp.com", "wrong"))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(CoreErrorCode.INVALID_CREDENTIALS));
    }

    @Test
    void refreshInvalidGrantThrowsInvalidRefreshToken() {
        server.expect(requestTo(TOKEN_URL))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"error\":\"invalid_grant\"}"));

        assertThatThrownBy(() -> client.refresh("stale"))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(CoreErrorCode.INVALID_REFRESH_TOKEN));
    }

    @Test
    void logoutSwallowsInvalidGrant() {
        server.expect(requestTo(LOGOUT_URL))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        assertThatCode(() -> client.logout("stale")).doesNotThrowAnyException();
    }
}