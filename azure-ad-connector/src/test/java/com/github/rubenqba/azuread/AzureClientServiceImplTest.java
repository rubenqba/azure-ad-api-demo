package com.github.rubenqba.azuread;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.github.rubenqba.azuread.model.CustomUser;
import com.github.rubenqba.azuread.service.AzureClientServiceImpl;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AzureClientServiceImplTest {

    private AzureClientServiceImpl azureClientService;

    @BeforeEach
    void setUp() {
        final String[] scopes = new String[] { "https://graph.microsoft.com/.default" };
        final ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                .build();
        azureClientService = new AzureClientServiceImpl("7382824e0b2f4cbb9d81c701d8921f1a", new GraphServiceClient(credential, scopes));
    }

    @Test
    void getUsers() {
        List<CustomUser> users = azureClientService.getUsers();
        users.forEach(System.out::println);
        assertThat(users).isNotEmpty();
    }

}