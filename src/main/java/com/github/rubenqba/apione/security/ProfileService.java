package com.github.rubenqba.apione.security;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class ProfileService {

    private static final Map<String, Set<String>> partners = Map.of(
            "partner1", Set.of("user1", "user2"),
            "partner2", Set.of("user3", "user4")
    );

    private static final Map<String, Set<String>> userCampaigns = Map.of(
            "user1", Set.of("campaign1", "campaign2"),
            "user2", Set.of("campaign3", "campaign4"),
            "user3", Set.of("campaign5", "campaign6"),
            "user4", Set.of("campaign7", "campaign8")
    );

    /**
     * Check if the user belongs to the partner.
     *
     * @param username  the username
     * @param partnerId the partner id
     * @return true if the user belongs to the partner
     */
    public boolean isUserBelongToPartner(String username, String partnerId) {
        return partners.containsKey(partnerId) && partners.get(partnerId).contains(username);
    }

    /**
     * Check if the user owns the campaign.
     *
     * @param username   the username
     * @param campaignId the campaign id
     * @return true if the user owns the campaign
     */
    public boolean isCampaignOwnsByUser(String username, String campaignId) {
        return userCampaigns.containsKey(username) && userCampaigns.get(username).contains(campaignId);
    }
}
