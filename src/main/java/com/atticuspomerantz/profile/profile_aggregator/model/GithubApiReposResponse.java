package com.atticuspomerantz.profile.profile_aggregator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/*
 * This class is a model for the response from the GitHub API when fetching a user's repos.
 * It uses Jackson annotations to map the JSON response to the fields in the acceptance criteria.
 */
@Data
public class GithubApiReposResponse {
    private String name; //name

    // incoming JSON field name: html_url
    // resulting JSON field name: url
    @JsonProperty("html_url")
    private String url; //url
}
