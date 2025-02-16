package com.atticuspomerantz.profile.profile_aggregator.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

/**
 * Represents a GitHub user profile as returned to the client.
 * Keeps Java camelCase naming conventions while mapping to the desired JSON field names.
 */
@Getter
@Builder
public class GithubUserProfile {

    @JsonProperty("user_name")
    private final String userName;

    @JsonProperty("display_name")
    private final String displayName;

    @JsonProperty("avatar")
    private final String avatar;

    @JsonProperty("geo_location")
    private final String geoLocation;

    @JsonProperty("email")
    private final String email;

    @JsonProperty("url")
    private final String url;

    @JsonProperty("created_at")
    private final LocalDateTime createdAt;

    @JsonProperty("repos")
    private final List<GithubUserRepo> repos;
}