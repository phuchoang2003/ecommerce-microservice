package com.hdp.common.security.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;

class HdpSecurityAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withUserConfiguration(TestSecurityConfig.class)
            .withConfiguration(AutoConfigurations.of(HdpSecurityAutoConfiguration.class))
            .withBean(JsonMapper.class, () -> JsonMapper.builder().build())
            .withBean(MessageSource.class, StaticMessageSource::new)
            .withPropertyValues(
                    "app.security.issuer-uri=http://localhost:8282/realms/hdp",
                    "app.security.jwk-set-uri=http://localhost:8282/realms/hdp/protocol/openid-connect/certs",
                    "app.security.permit-all-get=/v1/products/**");

    @Test
    void createsFilterChainAndJwtDecoderFromConfiguration() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).hasSingleBean(SecurityFilterChain.class);
            assertThat(context).hasSingleBean(JwtDecoder.class);
        });
    }

    @Test
    void backsOffWhenSecurityDisabled() {
        contextRunner
                .withPropertyValues("app.security.enabled=false")
                .run(context -> {
                    assertThat(context).hasNotFailed();
                    assertThat(context).doesNotHaveBean(SecurityFilterChain.class);
                });
    }

    @Configuration
    @EnableWebSecurity
    static class TestSecurityConfig {
    }
}