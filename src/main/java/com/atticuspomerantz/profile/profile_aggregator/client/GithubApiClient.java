package com.atticuspomerantz.profile.profile_aggregator.client;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.atticuspomerantz.profile.profile_aggregator.config.GithubApiConfig;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiReposResponse;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiUserResponse;
import com.atticuspomerantz.profile.profile_aggregator.util.ApiConstants;


@Service
public class GithubApiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GithubApiClient.class);
    private final Duration timeoutDuration;
    private final WebClient webClient;

    public GithubApiClient(GithubApiConfig githubApiConfig, WebClient.Builder webClientBuilder) {
        this.timeoutDuration = githubApiConfig.getTimeoutDuration();
        this.webClient = webClientBuilder.baseUrl(ApiConstants.GITHUB_BASE_URL).build();
    }

    /**
     * Fetches GitHub user details synchronously.
     *
     * @param username GitHub username.
     * @return GithubApiUserResponse or null if the request fails.
     */
    public GithubApiUserResponse fetchUserDetails(String username) {
        try {
            GithubApiUserResponse userResponse = webClient.get()
                    .uri(ApiConstants.GITHUB_USER_ENDPOINT, username)
                    .retrieve()
                    .bodyToMono(GithubApiUserResponse.class)
                    .block(timeoutDuration);

            if (userResponse == null) {
                return null;
            }
            return userResponse;
        } catch (WebClientResponseException ex) {
            LOGGER.warn("GitHub API error {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
        } catch (Exception ex) {
            LOGGER.warn("Unexpected error fetching user details for {}: {} ", username, ex.getMessage());
        }
        return null;
    }

    /**
     * Fetches GitHub user repositories synchronously.
     *
     * @param username GitHub username.
     * @return A list of repositories or an empty list if the request fails.
     */
    public List<GithubApiReposResponse> fetchUserRepositories(String username) {
        try {
            GithubApiReposResponse[] repos = webClient.get()
                    .uri(ApiConstants.GITHUB_REPOS_ENDPOINT, username)
                    .retrieve()
                    .bodyToMono(GithubApiReposResponse[].class)
                    .block(timeoutDuration);

            if (repos == null) {
                return new ArrayList<>();
            }
            return List.of(repos);
        } catch (WebClientResponseException ex) {
            LOGGER.warn("GitHub API error {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
        } catch (Exception ex) {
            LOGGER.warn("Unexpected error fetching user details for {}: {} ", username, ex.getMessage());
        }
        return new ArrayList<>();
    }
}
