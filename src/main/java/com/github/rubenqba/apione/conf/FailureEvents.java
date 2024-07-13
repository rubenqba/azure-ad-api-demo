package com.github.rubenqba.apione.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class FailureEvents {
    private static final Logger log = LoggerFactory.getLogger(FailureEvents.class);

	@EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent badCredentials) {
		if (badCredentials.getAuthentication() instanceof BearerTokenAuthenticationToken) {
		    log.error("Bad credentials: {}", badCredentials.getAuthentication());
        }
    }
}