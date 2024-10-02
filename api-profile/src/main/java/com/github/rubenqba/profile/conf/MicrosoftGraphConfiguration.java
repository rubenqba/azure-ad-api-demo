package com.github.rubenqba.profile.conf;

import com.azure.identity.ClientSecretCredentialBuilder;
import com.github.rubenqba.azuread.service.AzureClientService;
import com.github.rubenqba.azuread.service.AzureClientServiceImpl;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MicrosoftGraphConfiguration summary here...
 *
 * @author rbresler
 **/
@Configuration
@EnableConfigurationProperties({MicrosoftGraphConfiguration.AzureTenantProperties.class, MicrosoftGraphConfiguration.AzureGraphClientProperties.class})
public class MicrosoftGraphConfiguration {

    @ConfigurationProperties(prefix = "security.azure.tenant")
    public record AzureTenantProperties(String id, String name, String defaultUserFlow) {};

    @ConfigurationProperties(prefix = "security.dependencies.azure-graph-client")
    public record AzureGraphClientProperties(String clientId, String clientSecret, String directoryExtension) {};

    @Bean
    @ConditionalOnMissingBean
    AzureClientService azureClientService(AzureTenantProperties tenantProperties, AzureGraphClientProperties clientProperties) {
        var credentials = new ClientSecretCredentialBuilder()
                .tenantId(tenantProperties.id())
                .clientId(clientProperties.clientId())
                .clientSecret(clientProperties.clientSecret())
                .build();

        final String[] scopes = new String[]{"https://graph.microsoft.com/.default"};
        var client = new GraphServiceClient(credentials, scopes);
        return new AzureClientServiceImpl(clientProperties.directoryExtension(), client);
    }
}
