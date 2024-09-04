package com.github.rubenqba.azuread;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.github.rubenqba.azuread.model.CustomUser;
import com.github.rubenqba.azuread.model.TeamSummary;
import com.github.rubenqba.azuread.service.AzureClientServiceImpl;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AzureClientServiceImplTest {

    @Value("${GRAPH_CLIENT_ID}")
    private String client;
    @Value("${GRAPH_CLIENT_SECRET}")
    private String secret;
    @Value("${GRAPH_TENANT_ID}")
    private String tenant;
    @Value("${GRAPH_DIRECTORY_EXTENSION}")
    private String directoryExtension;

    private AzureClientServiceImpl azureClientService;

    @BeforeEach
    void setUp() {
        final String[] scopes = new String[]{"https://graph.microsoft.com/.default"};
        final ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                .clientId(client)
                .clientSecret(secret)
                .tenantId(tenant)
                .build();
        final var graphServiceClient = new GraphServiceClient(credential, scopes);

        azureClientService = new AzureClientServiceImpl(directoryExtension, graphServiceClient);
    }

    @Test
    void getUsers() {
        List<CustomUser> users = azureClientService.getUsers();
        users.forEach(System.out::println);
        assertThat(users).isNotEmpty();
    }

    @Test
    void updateUserSubscription() {
        String id = "aca94c78-9773-4d4d-9bf9-47f874f61463";
        Optional<CustomUser> users = azureClientService.findUser(id);
        assertThat(users).isNotEmpty();

        azureClientService.updateUserSubscription(id, new TeamSummary("team_a", "Team A", "startup_monthly"), new HashSet<>(List.of("buyer", "staff")));
        assertThat(azureClientService.findUser(id)).isNotEmpty()
                .hasValueSatisfying(user -> {
                    assertThat(user.team().id()).isEqualTo("team_a");
                    assertThat(user.team().name()).isEqualTo("Team A");
                    assertThat(user.team().subscription()).isEqualTo("startup_monthly");
                });
    }

    @Configuration
    static class EmptyConfiguration {
    }
}