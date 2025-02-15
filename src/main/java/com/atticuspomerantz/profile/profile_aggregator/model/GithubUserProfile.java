package com.atticuspomerantz.profile.profile_aggregator.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

/**
 * Represents a GitHub user profile as returned to the client
 */
@Getter
@Builder
public class GithubUserProfile {
    private final String user_name;
    private final String display_name;
    private final String avatar;
    private final String geo_location;
    private final String email; 
    private final String url;
    private final LocalDateTime created_at;
    private final List<GithubUserRepo> repos;
}