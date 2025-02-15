package com.atticuspomerantz.profile.profile_aggregator.client;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiReposResponse;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiUserResponse;
import com.atticuspomerantz.profile.profile_aggregator.util.ApiConstants;


@Service
public class GithubApiClient {

    private final WebClient webClient;

    public GithubApiClient(WebClient.Builder webClientBuilder) {
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
            return webClient.get()
                    .uri("/users/{username}", username)
                    .retrieve()
                    .bodyToMono(GithubApiUserResponse.class)
                    .block(); // Synchronous call for simplicity
        } catch (Exception ex) {
            System.err.println("Error fetching user details: " + ex.getMessage());
            return null;
        }
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
                    .uri("/users/{username}/repos", username)
                    .retrieve()
                    .bodyToMono(GithubApiReposResponse[].class)
                    .block(); // Synchronous call for simplicity

            return repos != null ? List.of(repos) : List.of();
        } catch (Exception ex) {
            System.err.println("Error fetching repositories: " + ex.getMessage());
            return List.of();
        }
    }
}
