package com.github.rubenqba.profile.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@PreAuthorize("hasAnyAuthority('AZ_Admin')")
public class ProfileCtrl {

    @GetMapping("/profiles/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal Jwt user) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return ResponseEntity.ok(Map.of("principal", authentication.getPrincipal(), "authorities", authentication.getAuthorities()));
    }

    @DeleteMapping("/profiles/{id}")
    @PreAuthorize("hasAuthority('AZ_Admin')")
    public ResponseEntity<?> deleteCurrentUser(@PathVariable String id, @AuthenticationPrincipal OidcUser user) {
        return ResponseEntity.ok(Map.of("success", true, "user", id, "deletedBy", user));
    }
}
