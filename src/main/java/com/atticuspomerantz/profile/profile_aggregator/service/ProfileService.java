package com.atticuspomerantz.profile.profile_aggregator.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.atticuspomerantz.profile.profile_aggregator.client.GithubApiClient;
import com.atticuspomerantz.profile.profile_aggregator.exception.UserNotFoundException;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiReposResponse;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiUserResponse;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserProfile;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserRepo;
import com.atticuspomerantz.profile.profile_aggregator.util.CacheUtility;

@Service
public class ProfileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileService.class);
    private final GithubApiClient githubApiClient;
    private final CacheUtility<GithubUserProfile> cache;

    /*
     * Additional clients could be added here (e.g. Gitlab). 
     * They could be composed in a more dynamic way, but for now, this is sufficient.
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
     * @throws UserNotFoundException if the user is not found.
     */
    public GithubUserProfile getGithubProfile(String username) {
        // Check cache first
        GithubUserProfile cachedProfile = cache.get(username);
        if (cachedProfile != null) {
            LOGGER.info("Cache hit for user: {}", username);
            return cachedProfile;
        }
        LOGGER.info("Cache miss for user: {}", username);
        // Fetch user details. Don't continue to repos if no user found.
        GithubApiUserResponse userResponse = githubApiClient.fetchUserDetails(username);
        if (userResponse == null) {
            LOGGER.info("GitHub user '{}' not found.", username);
            throw new UserNotFoundException(username);
        }

        // Fetch repositories 
        List<GithubApiReposResponse> reposResponse;
        try {
            reposResponse = githubApiClient.fetchUserRepositories(username);
        } catch (Exception e) {
            LOGGER.warn("Failed to fetch repos for user '{}': {}", username, e.getMessage());
            reposResponse = List.of();  // Handle failure gracefully by returning an empty list
        }

        // Build and cache the final profile
        GithubUserProfile profile = buildGithubUserProfile(userResponse, reposResponse);
        cache.put(username, profile);
        return profile;

    }

    /**
     * Builds a GithubUserProfile object from the API responses.
     * 
     * @param userResponse
     * @param reposResponse
     * @return GithubUserProfile
     */
    private GithubUserProfile buildGithubUserProfile(GithubApiUserResponse userResponse, List<GithubApiReposResponse> reposResponse) {
        return GithubUserProfile.builder()
                .userName(userResponse.getUserName())
                .displayName(userResponse.getDisplayName())
                .avatar(userResponse.getAvatar())
                .geoLocation(userResponse.getGeoLocation())
                .email(userResponse.getEmail())
                .url(userResponse.getUrl())
                .createdAt(userResponse.getCreatedAt())
                .repos(reposResponse.stream()
                    .map(repo -> GithubUserRepo.builder()
                        .name(repo.getName())
                        .url(repo.getUrl())
                        .build())
                    .toList())
                .build();
    }
}
