package com.atticuspomerantz.profile.profile_aggregator.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.atticuspomerantz.profile.profile_aggregator.client.GithubApiClient;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserProfile;

@Service
public class ProfileService {
    private final GithubApiClient githubApiClient;

    public ProfileService(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient;
    }

    /**
     * Fetches the GitHub profile for a given username.
     *
     * @param username GitHub username.
     * @return A `GithubUserProfile` object or null if not found.
     */
    public GithubUserProfile getGithubProfile(String username) {
        CompletableFuture<GithubUserProfile> githubProfileFuture = githubApiClient.getProfile(username);
        
        try {
            return githubProfileFuture.join(); // Block until result is available
        } catch (Exception ex) {
            System.err.println("Error fetching GitHub profile: " + ex.getMessage());
            return null; // Fail gracefully
        }
    }

}
