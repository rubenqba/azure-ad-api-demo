package com.github.rubenqba.profile.conf;

import com.azure.identity.ClientSecretCredentialBuilder;
import com.github.rubenqba.azuread.service.AzureClientService;
import com.github.rubenqba.azuread.service.AzureClientServiceImpl;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * MicrosoftGraphConfiguration summary here...
 *
 * @author rbresler
 **/
@Configuration
public class MicrosoftGraphConfiguration {

    @Bean
    @ConditionalOnMissingBean
    AzureClientService azureClientService(Environment env) {
        var extension = env.getRequiredProperty("azure.graph.directory-extension");
        final String[] scopes = new String[]{"https://graph.microsoft.com/.default"};
        var credentials = new ClientSecretCredentialBuilder()
                .tenantId(env.getRequiredProperty("azure.tenant.id"))
                .clientId(env.getRequiredProperty("azure.graph.client-id"))
                .clientSecret(env.getRequiredProperty("azure.graph.client-secret"))
                .build();

        var client = new GraphServiceClient(credentials, scopes);
        return new AzureClientServiceImpl(extension, client);
    }
}
