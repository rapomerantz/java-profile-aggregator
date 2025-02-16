package com.atticuspomerantz.profile.profile_aggregator.util;


public final class ApiConstants {
    private ApiConstants() {}

    public static final String GITHUB_BASE_URL = "https://api.github.com";
    public static final String GITHUB_USER_ENDPOINT = "/users/{username}";
    public static final String GITHUB_REPOS_ENDPOINT = "/users/{username}/repos";
}