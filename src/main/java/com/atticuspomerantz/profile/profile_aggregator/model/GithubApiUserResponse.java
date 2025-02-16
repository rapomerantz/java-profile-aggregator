package com.atticuspomerantz.profile.profile_aggregator.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

/**
 * This class represents the GitHub API response for a user profile.
 * It uses Jackson annotations to map the JSON fields correctly.
 */
@Data
@Builder
public class GithubApiUserResponse {

    @JsonProperty("login")
    private String userName;

    @JsonProperty("name")
    private String displayName;

    @JsonProperty("avatar_url")
    private String avatar;

    @JsonProperty("location")
    private String geoLocation;

    private String email; // No mapping necessary

    @JsonProperty("html_url")
    private String url;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
