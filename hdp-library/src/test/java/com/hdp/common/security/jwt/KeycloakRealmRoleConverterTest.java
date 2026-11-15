package com.hdp.common.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class KeycloakRealmRoleConverterTest {

    private final KeycloakRealmRoleConverter converter = new KeycloakRealmRoleConverter("hdp-backend");

    @Test
    void mapsRealmRolesToRoleAuthorities() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user-1")
                .claim("realm_access", Map.of("roles", List.of("BUYER", "ADMIN")))
                .build();

        assertThat(authorities(converter.convert(jwt))).containsExactlyInAnyOrder("ROLE_BUYER", "ROLE_ADMIN");
    }

    @Test
    void mapsResourceAccessRolesForConfiguredClientOnly() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user-1")
                .claim("resource_access", Map.of(
                        "hdp-backend", Map.of("roles", List.of("seller")),
                        "other-client", Map.of("roles", List.of("IGNORED"))))
                .build();

        assertThat(authorities(converter.convert(jwt))).containsExactly("ROLE_SELLER");
    }

    @Test
    void returnsEmptyForMissingClaims() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user-1")
                .build();

        assertThat(converter.convert(jwt)).isEmpty();
    }

    private static Collection<String> authorities(Collection<GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).toList();
    }
}