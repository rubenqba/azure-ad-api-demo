package com.github.rubenqba.apione.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class OwnershipOrAdminAspect {

    private final ProfileService profileService;

    public OwnershipOrAdminAspect(ProfileService profileService) {
        this.profileService = profileService;
    }

    public boolean checkOwnership(String campaignId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        var authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("Admin"))) {
            return true;
        }

        if (authentication instanceof JwtAuthenticationToken token) {
            String currentUser = token.getName();

            // Se verifica si el usuario actual es dueño de la campaña
            if (profileService.isCampaignOwnsByUser(currentUser, campaignId)) {
                return true;
            }
            // Si no es igual se verifica si el usuario actual tiene permisos de `USER_STAFF` sobre el partner actual
            String roles = token.getTokenAttributes().get("extension_PartnerRole").toString();
            return roles.contains("USER_STAFF");
        }

        return false;
    }

    @Around("@annotation(CheckOwnershipOrAdmin)")
    public Object checkOwnershipOrAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        // verificar si existe algun parametro de nombre `user` o `userId`
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature methodSignature) {
            String[] parameterNames = methodSignature.getParameterNames();
            Object[] parameterValues = joinPoint.getArgs();

            for (int i = 0; i < parameterNames.length; i++) {
                if (("campaign".equals(parameterNames[i]) || "id".equals(parameterNames[i])) && parameterValues[i] instanceof String campaignId) {
                   if (!checkOwnership(campaignId)) {
                       // lanzar una excepcion de acceso no autorizado
                       throw new IllegalAccessException("Access denied, you are not the owner of the campaign");
                   }
                   break;
                }
            }
        }

        return joinPoint.proceed();
    }
}
