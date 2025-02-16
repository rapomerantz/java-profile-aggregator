package com.atticuspomerantz.profile.profile_aggregator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

/**
 * Represents a repository in the final API response.
 */
@Getter
@Builder
public class GithubUserRepo {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("url")
    private final String url;
}