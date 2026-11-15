package com.hdp.common.security.config;

import com.hdp.common.security.jwt.KeycloakRealmRoleConverter;
import com.hdp.common.web.cors.CorsProperties;
import com.hdp.common.security.web.JwtUserIdMdcFilter;
import com.hdp.common.security.web.SecurityExceptionHandler;
import com.hdp.common.security.web.SecurityProblemWriter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

/**
 * Keycloak JWT resource server for any service that has Spring Security on its classpath.
 *
 * <p>Enabled by default when the security starters are present; disable with
 * {@code app.security.enabled=false}.</p>
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({SecurityFilterChain.class, JwtDecoder.class, JwtAuthenticationToken.class})
@ConditionalOnProperty(prefix = "app.security", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({HdpSecurityProperties.class, CorsProperties.class})
@EnableMethodSecurity
public class HdpSecurityAutoConfiguration {

    private static final String[] ALWAYS_PUBLIC = {
            "/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/health",
            "/actuator/health/**",
            "/actuator/info",
            "/error"
    };

    @Bean
    @ConditionalOnMissingBean
    public JwtDecoder jwtDecoder(HdpSecurityProperties properties) {
        String issuerUri = properties.issuerUri();
        if (!StringUtils.hasText(issuerUri)) {
            throw new IllegalStateException(
                    "app.security.issuer-uri is required when Spring Security is on the classpath");
        }
        if (StringUtils.hasText(properties.jwkSetUri())) {
            NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(properties.jwkSetUri()).build();
            decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuerUri));
            return decoder;
        }
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationConverter jwtAuthenticationConverter(HdpSecurityProperties properties) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(
                new KeycloakRealmRoleConverter(properties.keycloak().clientId()));
        return converter;
    }

    /**
     * Security 7 requires a CorsConfigurationSource bean when CORS is enabled.
     * Registered for every path: matchers are relative to server.servlet.context-path,
     * so the legacy {@code app.cors.mapping} (/api/**) would not match.
     */
    @Bean
    @ConditionalOnMissingBean
    public CorsConfigurationSource corsConfigurationSource(CorsProperties properties) {
        CorsConfiguration configuration = new CorsConfiguration();
        // allowedOriginPatterns, not allowedOrigins: "*" plus allowCredentials is rejected otherwise.
        configuration.setAllowedOriginPatterns(properties.allowedOrigins());
        configuration.setAllowedMethods(properties.allowedMethods());
        configuration.setAllowedHeaders(properties.allowedHeaders());
        configuration.setExposedHeaders(properties.exposedHeaders());
        configuration.setAllowCredentials(properties.allowCredentials());
        configuration.setMaxAge(properties.maxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityProblemWriter securityProblemWriter(JsonMapper jsonMapper, MessageSource messageSource) {
        return new SecurityProblemWriter(jsonMapper, messageSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityExceptionHandler securityExceptionHandler(MessageSource messageSource) {
        return new SecurityExceptionHandler(messageSource);
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain hdpSecurityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthenticationConverter,
            SecurityProblemWriter securityProblemWriter,
            HdpSecurityProperties properties) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(ALWAYS_PUBLIC).permitAll();
                    permitAll(authorize, properties.permitAll());
                    permitGet(authorize, properties.permitAllGet());
                    permitPost(authorize, properties.permitAllPost());
                    authorize.anyRequest().authenticated();
                })
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                        .authenticationEntryPoint(securityProblemWriter)
                        .accessDeniedHandler(securityProblemWriter))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(securityProblemWriter)
                        .accessDeniedHandler(securityProblemWriter));

        http.addFilterAfter(new JwtUserIdMdcFilter(), BearerTokenAuthenticationFilter.class);
        return http.build();
    }

    private static void permitAll(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize,
            List<String> patterns) {
        if (!patterns.isEmpty()) {
            authorize.requestMatchers(patterns.toArray(String[]::new)).permitAll();
        }
    }

    private static void permitGet(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize,
            List<String> patterns) {
        if (!patterns.isEmpty()) {
            authorize.requestMatchers(HttpMethod.GET, patterns.toArray(String[]::new)).permitAll();
        }
    }

    private static void permitPost(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize,
            List<String> patterns) {
        if (!patterns.isEmpty()) {
            authorize.requestMatchers(HttpMethod.POST, patterns.toArray(String[]::new)).permitAll();
        }
    }
}