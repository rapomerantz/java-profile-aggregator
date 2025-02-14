package com.atticuspomerantz.profile.profile_aggregator.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GithubUserProfile {
    private final String user_name;
    private final String email; 
    private final String display_name;
    private final String avatar;
    private final String geo_location;
    private final String url;
    private final LocalDateTime created_at;
    private final GithubUserRepo[] repos;
}