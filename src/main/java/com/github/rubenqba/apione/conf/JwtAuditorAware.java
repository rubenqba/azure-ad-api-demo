package com.github.rubenqba.apione.conf;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class JwtAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Optional<String> auditor = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(Jwt.class::cast)
                .map(this::getAuditorName);
        return auditor;
    }

    private String getAuditorName(Jwt principal) {
        return Optional.ofNullable(principal.getClaimAsString("name"))
                .orElseGet(principal::getSubject);
    }
}
