package com.atticuspomerantz.profile.profile_aggregator.service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.atticuspomerantz.profile.profile_aggregator.client.GithubApiClient;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiReposResponse;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiUserResponse;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserProfile;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserRepo;
import com.atticuspomerantz.profile.profile_aggregator.util.CacheUtility;

@Service
public class ProfileService {
    private static final Logger LOGGER = Logger.getLogger(ProfileService.class.getName());
    private final GithubApiClient githubApiClient;
    private final CacheUtility<GithubUserProfile> cache;

    /*
     * Additional clients could be added here in the future. 
     * They could be built in a more dynamic (and more complex) way, but for now, this is sufficient.
     */
    public ProfileService(GithubApiClient githubApiClient, CacheUtility<GithubUserProfile> cache) {
        this.githubApiClient = githubApiClient;
        this.cache = cache;
    }

    /**
     * Fetches the GitHub profile for a given username.
     * Uses synchronous calls to fetch user details and repositories for simplicity (but could be updated to use async calls).
     *
     * @param username GitHub username.
     * @return A GithubUserProfile object or null if not found.
     */
    public GithubUserProfile getGithubProfile(String username) {
        // Check cache first
        GithubUserProfile cachedProfile = cache.get(username);
        if (cachedProfile != null) {
            LOGGER.info("Cache hit for user: " + username);
            return cachedProfile;
        }

        try {
            // Fetch user details and repositories synchronously
            GithubApiUserResponse userResponse = githubApiClient.fetchUserDetails(username);
            List<GithubApiReposResponse> reposResponse = githubApiClient.fetchUserRepositories(username);

            if (userResponse == null) {
                LOGGER.info("GitHub user not found: " + username);
                return null;
            }

            // Build and cache the final profile
            GithubUserProfile profile = buildGithubUserProfile(userResponse, reposResponse);
            cache.put(username, profile);
            return profile;
        } catch (Exception ex) {
            LOGGER.info("Error fetching GitHub profile for " + username + ": " + ex.getMessage());
            return null;
        }
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
