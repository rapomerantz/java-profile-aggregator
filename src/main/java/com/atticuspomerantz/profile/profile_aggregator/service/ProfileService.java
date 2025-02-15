package com.atticuspomerantz.profile.profile_aggregator.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.atticuspomerantz.profile.profile_aggregator.client.GithubApiClient;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiReposResponse;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiUserResponse;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserProfile;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserRepo;

@Service
public class ProfileService {
    private final GithubApiClient githubApiClient;

    /*
     * Additional clients could be added here in the future. 
     * They could be built in a more dynamic (and more complex) way, but for now, this is sufficient.
     */
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
        CompletableFuture<GithubApiUserResponse> userFuture = githubApiClient.fetchUserDetails(username);
        CompletableFuture<List<GithubApiReposResponse>> reposFuture = githubApiClient.fetchUserRepositories(username);

        return userFuture.thenCombine(reposFuture, this::buildGithubUserProfile)
                .exceptionally(ex -> {
                    System.err.println("Error fetching GitHub profile: " + ex.getMessage());
                    return null;
                })
                .join();
    }

    /** Converts API response objects into the final GithubUserProfile */
    private GithubUserProfile buildGithubUserProfile(
            GithubApiUserResponse userResponse,
            List<GithubApiReposResponse> reposResponse) {

        return GithubUserProfile.builder()
                .user_name(userResponse.getUser_name())
                .display_name(userResponse.getDisplay_name())
                .avatar(userResponse.getAvatar())
                .geo_location(userResponse.getGeo_location())
                .email(userResponse.getEmail())
                .url(userResponse.getUrl())
                .created_at(userResponse.getCreated_at())
                .repos(reposResponse.stream()
                        .map(repo -> GithubUserRepo.builder()
                                .name(repo.getName())
                                .url(repo.getUrl())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
