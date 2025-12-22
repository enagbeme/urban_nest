package com.urbannest.backend.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleBasedAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        String requestUri = request.getRequestURI();
        if (requestUri != null && requestUri.startsWith("/api/")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            boolean isHomeowner = authentication.getAuthorities().stream()
                    .anyMatch(a -> "HOMEOWNER".equals(a.getAuthority()));
            boolean isClient = authentication.getAuthorities().stream()
                    .anyMatch(a -> "CLIENT".equals(a.getAuthority()));

            if (isHomeowner) {
                response.sendRedirect("/homeowner-dashboard");
                return;
            }
            if (isClient) {
                response.sendRedirect("/client-dashboard");
                return;
            }
        }

        response.sendRedirect("/login");
    }
}
