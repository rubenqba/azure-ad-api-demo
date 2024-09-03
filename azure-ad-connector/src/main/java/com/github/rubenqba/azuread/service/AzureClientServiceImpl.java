package com.github.rubenqba.azuread.service;

import com.github.rubenqba.azuread.model.CustomUser;
import com.github.rubenqba.azuread.model.Summary;
import com.microsoft.graph.models.ObjectIdentity;
import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Stream;


public class AzureClientServiceImpl implements AzureClientService {

    private final GraphServiceClient msfClient;
    private final String directoryExtension;
    private final String[] userAttributes;

    public AzureClientServiceImpl(String directoryExtension, GraphServiceClient msfClient) {
        this.directoryExtension = directoryExtension;
        if (StringUtils.hasText(directoryExtension)) {
            var extension = directoryExtension.replace("-", "");
            this.userAttributes = new String[]{"id", "givenName", "surname", "country", "otherMails", "identities", "createdDateTime", "extension_" + extension + "_SubscriptionType", "extension_" + extension + "_PartnerRole", "extension_" + extension + "_PartnerName", "extension_" + extension + "_PartnerID"};
        } else {
            this.userAttributes = new String[]{"id", "givenName", "surname", "country", "otherMails", "identities", "createdDateTime"};
        }
        this.msfClient = msfClient;
    }

    @Override
    public List<CustomUser> getUsers() {
        return msfClient.users().get(config -> {
            config.queryParameters.select = userAttributes;
        }).getValue().stream().map(this::mapUser).toList();
    }

    private CustomUser mapUser(User user) {
        System.out.println("User: " + user.getGivenName() + " " + user.getSurname());
        var email = extractUserEmails(user).findFirst().orElse(null);
        var team = CollectionUtils.isEmpty(user.getAdditionalData()) ? null : mapTeam(user.getAdditionalData());
        var roles = CollectionUtils.isEmpty(user.getAdditionalData()) ? null : mapRoles(user.getAdditionalData());
        var subscription = CollectionUtils.isEmpty(user.getAdditionalData()) ? null : mapSubscription(user.getAdditionalData());

        return new CustomUser(user.getId(), user.getGivenName(), user.getSurname(), email, user.getMail(), team, roles);
    }

    private Stream<String> extractUserEmails(User user) {
        Stream<String> otherMails = CollectionUtils.isEmpty(user.getOtherMails()) ? Stream.empty() : user.getOtherMails().stream();
        Stream<String> identities = CollectionUtils.isEmpty(user.getIdentities()) ? Stream.empty() : user.getIdentities().stream()
                .filter(id -> "emailAddress".equalsIgnoreCase(id.getSignInType()))
                .map(ObjectIdentity::getIssuerAssignedId);
        return Stream.concat(otherMails, identities);
    }

    private String mapSubscription(Map<String, Object> data) {
        return data.getOrDefault("extension_" + directoryExtension + "_SubscriptionType", "").toString();
    }

    private Summary mapTeam(Map<String, Object> data) {
        var id = data.getOrDefault("extension_" + directoryExtension + "_PartnerID", "").toString();
        var team = data.getOrDefault("extension_" + directoryExtension + "_PartnerName", "").toString();
        return StringUtils.hasText(id) ? new Summary(id, team) : null;
    }

    private Set<String> mapRoles(Map<String, Object> data) {
        var roles = data.getOrDefault("extension_" + directoryExtension + "_PartnerRole", "").toString();
        return new HashSet<>(Arrays.asList(StringUtils.hasText(roles) ? roles.split(",") : new String[]{"buyer"}));
    }

    @Override
    public CustomUser updateUser(CustomUser user) {
        User stored = new User();
        HashMap<String, Object> additionalData = new HashMap<String, Object>();
        additionalData.put("extension_" + directoryExtension + "_SubscriptionType", null);
        additionalData.put("extension_" + directoryExtension + "_PartnerRole", String.join(",", user.roles()));
        additionalData.put("extension_" + directoryExtension + "_PartnerID", user.team().id());
        additionalData.put("extension_" + directoryExtension + "_PartnerName", user.team().name());
        stored.setAdditionalData(additionalData);
        User result = msfClient.users().byUserId(user.id()).patch(stored);
        return mapUser(result);
    }
}
