package com.hdp.common.security.principal;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Convenience accessor for the authenticated Keycloak user of the current request.
 *
 * <p>{@link #subject()} is the Keycloak user id, not the customer-service database id.</p>
 */
public final class CurrentUser {

    private static final String PRE_AUTH_ROLE = "ROLE_";

    private CurrentUser() {
    }

    public static Optional<Jwt> jwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken token) {
            return Optional.of(token.getToken());
        }
        return Optional.empty();
    }

    public static Optional<String> subject() {
        return jwt().map(Jwt::getSubject);
    }

    public static Set<String> roles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return Set.of();
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith(PRE_AUTH_ROLE))
                .map(authority -> authority.substring(PRE_AUTH_ROLE.length()))
                .collect(Collectors.toUnmodifiableSet());
    }

    public static boolean hasRole(String role) {
        return role != null && roles().contains(role.toUpperCase(Locale.ROOT));
    }
}