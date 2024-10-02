package com.github.rubenqba.profile.conf;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

/**
 * JwtCustomizerConfiguration summary here...
 *
 * @author rbresler
 **/
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
public class JwtCustomizerConfiguration {

    @Bean
    JwtAuthenticationConverter authenticationConverter(OAuth2ResourceServerProperties properties) {
        OAuth2ResourceServerProperties.Jwt jwt = properties.getJwt();
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();

        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        if (StringUtils.hasText(jwt.getAuthoritiesClaimName())) {
            authoritiesConverter.setAuthoritiesClaimName(jwt.getAuthoritiesClaimName());
        }
        if (StringUtils.hasText(jwt.getAuthorityPrefix())) {
            authoritiesConverter.setAuthorityPrefix(jwt.getAuthorityPrefix());
        }
        authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return authenticationConverter;
    }

    Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer> jwtConfigurer(JwtAuthenticationConverter customizer) {
        return jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(customizer);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, OAuth2ResourceServerProperties properties) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }).oauth2ResourceServer((oauth2) -> {
            oauth2.jwt(jwtConfigurer(authenticationConverter(properties)));
        });
        return http.build();
    }
}
