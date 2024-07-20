package com.github.rubenqba.apione.ctrl;

import com.github.rubenqba.apione.security.CheckOwnershipOrAdmin;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//@PreAuthorize("hasAnyAuthority('Admin', 'Read')")
public class UserCtrl {

    @GetMapping("/users/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal OidcUser user) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return ResponseEntity.ok(Map.of("principal", authentication.getPrincipal(), "authorities", authentication.getAuthorities()));
    }

    @GetMapping("/public")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<String> anonymous() {
        return ResponseEntity.ok("Hello Anonymous");
    }

    @GetMapping("/protected_write")
    @PreAuthorize("hasAnyAuthority('Admin', 'Write')")
    public ResponseEntity<String> protectedWrite() {
        return ResponseEntity.ok("Hello Protected Write");
    }

    @GetMapping("/protected_admin")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<String> protectedAdmin() {
        return ResponseEntity.ok("Hello Protected Admin");
    }

    @GetMapping("/protected_flow/{campaign}")
    @CheckOwnershipOrAdmin
    public ResponseEntity<?> protectedUser(@PathVariable String campaign) {
        return ResponseEntity.ok(Map.of("message", "Protected campaign changed"));
    }
}
