package com.github.rubenqba.profile.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class SecurityUtils {

    private static final String TEAM_ID_CLAIM = "extension_PartnerID";

    public static boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken user) {
            return isAdmin(user.getAuthorities());
        }
        throw new AuthenticationCredentialsNotFoundException("Missing authentication");
    }

    public static boolean isAdmin(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().anyMatch(a -> a.getAuthority().equals("AZ_Admin"));
    }

    public static String getCurrentUserTeam() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken user) {
            return getUserTeam(user.getToken());
        }
        throw new AuthenticationCredentialsNotFoundException("Missing authentication");
    }

    public static String getUserTeam(Jwt user) {
        if (user != null) {
            return user.getClaimAsString(TEAM_ID_CLAIM);
        }
        throw new UsernameNotFoundException("User was null");
    }
}
