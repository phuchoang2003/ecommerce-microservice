package com.hdp.common.security.web;

import com.hdp.common.security.dto.LoginRequest;
import com.hdp.common.security.dto.LoginResponse;
import com.hdp.common.security.token.KeycloakTokenClient;
import com.hdp.common.security.token.TokenResponse;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private final KeycloakTokenClient tokenClient = mock(KeycloakTokenClient.class);
    private final AuthController controller = new AuthController(tokenClient);

    @Test
    void loginUsesEmailAsUsername() {
        when(tokenClient.login("buyer@hdp.com", "pw"))
                .thenReturn(new TokenResponse("access", "refresh", "Bearer", 3600));

        LoginResponse response = controller.login(new LoginRequest("buyer@hdp.com", null, "pw")).data();

        verify(tokenClient).login("buyer@hdp.com", "pw");
        assertThat(response.accessToken()).isEqualTo("access");
        assertThat(response.refreshToken()).isEqualTo("refresh");
    }

    @Test
    void loginUsesPhoneWhenEmailMissing() {
        when(tokenClient.login("0901234567", "pw"))
                .thenReturn(new TokenResponse("access", "refresh", "Bearer", 3600));

        controller.login(new LoginRequest(null, "0901234567", "pw"));

        verify(tokenClient).login("0901234567", "pw");
    }

    @Test
    void serializesTokenFieldsCamelCaseUnderSnakeCaseMapper() {
        JsonMapper mapper = JsonMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();

        String json = mapper.writeValueAsString(new LoginResponse("access", "refresh", "Bearer", 3600));

        assertThat(json).contains("\"accessToken\":\"access\"");
        assertThat(json).contains("\"refreshToken\":\"refresh\"");
        assertThat(json).doesNotContain("access_token");
    }
}