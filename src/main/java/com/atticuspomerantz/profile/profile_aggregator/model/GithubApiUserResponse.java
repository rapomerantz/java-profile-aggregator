package com.atticuspomerantz.profile.profile_aggregator.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/*
 * This class is a model for the response from the GitHub API when fetching a user's profile.
 * It uses Jackson annotations to map the JSON response to the fields in the acceptance criteria.
 */
@Data
public class GithubApiUserResponse {
    // incoming JSON field name: login
    // resulting JSON field name: user_name
    @JsonProperty("login")
    private String user_name;

    @JsonProperty("name")
    private String display_name;

    @JsonProperty("avatar_url")
    private String avatar;

    @JsonProperty("location")
    private String geo_location;

    //no mapping necessary
    private String email;

    @JsonProperty("html_url")
    private String url;

    //using mapping to convert string to LocalDateTime
    @JsonProperty("created_at")
    private LocalDateTime created_at;
}
