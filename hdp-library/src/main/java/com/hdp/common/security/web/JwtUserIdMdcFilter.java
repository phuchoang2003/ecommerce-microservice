package com.hdp.common.security.web;

import com.hdp.core.constant.RequestContextConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Puts the Keycloak subject into MDC so logs and SQL comments carry the user id.
 */
public class JwtUserIdMdcFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String userId = currentSubject();
        if (userId == null) {
            filterChain.doFilter(request, response);
            return;
        }

        MDC.put(RequestContextConstants.USER_ID, userId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(RequestContextConstants.USER_ID);
        }
    }

    private static String currentSubject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken token) {
            return token.getToken().getSubject();
        }
        return null;
    }
}