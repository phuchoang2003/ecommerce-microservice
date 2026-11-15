package com.hdp.common.security.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Maps Keycloak realm roles and client-scoped roles to Spring Security authorities.
 */
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String ROLES_CLAIM = "roles";
    private static final String PRE_AUTH_ROLE = "ROLE_";

    private final String clientId;

    public KeycloakRealmRoleConverter(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Set<String> roles = new LinkedHashSet<>(rolesOf(jwt.getClaims().get("realm_access")));
        roles.addAll(clientRoles(jwt));

        return roles.stream()
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(
                        PRE_AUTH_ROLE + role.toUpperCase(Locale.ROOT)))
                .toList();
    }

    private Collection<String> clientRoles(Jwt jwt) {
        Object resourceAccess = jwt.getClaims().get("resource_access");
        if (!(resourceAccess instanceof Map<?, ?> clients)) {
            return List.of();
        }
        return rolesOf(clients.get(clientId));
    }

    private static Collection<String> rolesOf(Object claim) {
        if (!(claim instanceof Map<?, ?> map)) {
            return List.of();
        }
        if (!(map.get(ROLES_CLAIM) instanceof Collection<?> roles)) {
            return List.of();
        }
        return roles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList();
    }
}