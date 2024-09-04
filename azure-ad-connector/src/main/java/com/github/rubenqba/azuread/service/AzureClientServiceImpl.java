package com.github.rubenqba.azuread.service;

import com.github.rubenqba.azuread.model.CustomUser;
import com.github.rubenqba.azuread.model.TeamSummary;
import com.microsoft.graph.models.ObjectIdentity;
import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Stream;


public class AzureClientServiceImpl implements AzureClientService {

    private final GraphServiceClient msfClient;
    private final String directoryExtension;
    private final String[] userAttributes;

    public AzureClientServiceImpl(String directoryExtension, GraphServiceClient msfClient) {
        this.directoryExtension = directoryExtension.replace("-", "");
        if (StringUtils.hasText(directoryExtension)) {
            this.userAttributes = new String[]{
                    "id", "givenName", "surname", "country", "otherMails", "identities", "createdDateTime",
                    "extension_" + this.directoryExtension + "_SubscriptionType",
                    "extension_" + this.directoryExtension + "_PartnerRole",
                    "extension_" + this.directoryExtension + "_PartnerName",
                    "extension_" + this.directoryExtension + "_PartnerID"
            };
        } else {
            this.userAttributes = new String[]{"id", "givenName", "surname", "country", "otherMails", "identities", "createdDateTime"};
        }
        this.msfClient = msfClient;
    }

    @Override
    public List<CustomUser> getUsers() {
        return msfClient.users()
                .get(config -> {
                    config.queryParameters.select = userAttributes;
                })
                .getValue()
                .stream()
                .map(this::mapUser)
                .toList();
    }

    @Override
    public List<CustomUser> getTeamUsers(String team) {
        return msfClient.users()
                .get(config -> {
                    config.queryParameters.select = userAttributes;
                    config.queryParameters.filter = "extension_" + this.directoryExtension + "_PartnerID eq '" + team + "'";
                })
                .getValue()
                .stream()
                .map(this::mapUser)
                .toList();
    }

    @Override
    public Optional<CustomUser> findUser(String id) {
        return Optional.ofNullable(msfClient.users().byUserId(id).get(
                config -> config.queryParameters.select = userAttributes
        )).map(this::mapUser);
    }

    private CustomUser mapUser(User user) {
        if (user == null) return null;
        var email = extractUserEmails(user).findFirst().orElse(null);
        var team = CollectionUtils.isEmpty(user.getAdditionalData()) ? null : mapTeam(user.getAdditionalData());
        var roles = CollectionUtils.isEmpty(user.getAdditionalData()) ? null : mapRoles(user.getAdditionalData());
        var created = Optional.ofNullable(user.getCreatedDateTime()).map(OffsetDateTime::toInstant).orElse(null);

        return new CustomUser(user.getId(), user.getGivenName(), user.getSurname(), email, user.getMail(), team, roles, created);
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

    private TeamSummary mapTeam(Map<String, Object> data) {
        var id = data.getOrDefault("extension_" + directoryExtension + "_PartnerID", "").toString();
        var team = data.getOrDefault("extension_" + directoryExtension + "_PartnerName", "").toString();
        var subscription = mapSubscription(data);
        return StringUtils.hasText(id) ? new TeamSummary(id, team, subscription) : null;
    }

    private Set<String> mapRoles(Map<String, Object> data) {
        var roles = data.getOrDefault("extension_" + directoryExtension + "_PartnerRole", "").toString();
        return new HashSet<>(Arrays.asList(StringUtils.hasText(roles) ? roles.split(",") : new String[]{"buyer"}));
    }

    @Override
    public void updateUser(String id, String firstName, String lastName) {
        User stored = new User();
        stored.setGivenName(firstName);
        stored.setSurname(lastName);
        stored.setDisplayName(String.join(" ", firstName, lastName));
        msfClient.users().byUserId(id).patch(stored);
    }

    @Override
    public void updateUserSubscription(String id, TeamSummary team, Set<String> roles) {
        User stored = new User();
        HashMap<String, Object> additionalData = new HashMap<>();
        additionalData.put("extension_" + directoryExtension + "_PartnerID", team.id());
        additionalData.put("extension_" + directoryExtension + "_PartnerName", team.name());
        additionalData.put("extension_" + directoryExtension + "_SubscriptionType", team.subscription());
        additionalData.put("extension_" + directoryExtension + "_PartnerRole", String.join(",", roles));
        stored.setAdditionalData(additionalData);
        msfClient.users().byUserId(id).patch(stored);
    }

    @Override
    public void deleteUser(String id) {
        msfClient.users().byUserId(id).delete();
    }
}
