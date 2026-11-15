package com.hdp.common.security.config;

import com.hdp.common.security.token.KeycloakTokenClient;
import com.hdp.common.security.web.AuthController;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Registers the ready-made /v1/auth endpoints. Opt-in per service.
 */
@AutoConfiguration
@ConditionalOnClass(SecurityFilterChain.class)
@ConditionalOnProperty(prefix = "app.security", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(HdpSecurityProperties.class)
public class KeycloakAuthAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "app.security.auth", name = "enabled", havingValue = "true")
    public KeycloakTokenClient keycloakTokenClient(HdpSecurityProperties properties) {
        return new KeycloakTokenClient(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "app.security.auth", name = "enabled", havingValue = "true")
    public AuthController authController(KeycloakTokenClient keycloakTokenClient) {
        return new AuthController(keycloakTokenClient);
    }
}