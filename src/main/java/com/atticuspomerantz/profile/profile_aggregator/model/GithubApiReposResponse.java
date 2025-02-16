package com.atticuspomerantz.profile.profile_aggregator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a repository from the GitHub API response.
 * Uses Jackson annotations to map JSON fields correctly.
 */
@Data
@Builder
public class GithubApiReposResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("html_url")
    private String url;
}
