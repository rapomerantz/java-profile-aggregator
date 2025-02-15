package com.atticuspomerantz.profile.profile_aggregator.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
     * Fetches GitHub user details.
     * Field mapping is already done by the response model class using Jackson annotations.
     *
     * @param username GitHub username.
     * @return A CompletableFuture containing GithubApiUserResponse.
     */
    public CompletableFuture<GithubApiUserResponse> fetchUserDetails(String username) {
        return webClient.get()
                .uri("/users/{username}", username)
                .retrieve()
                .bodyToMono(GithubApiUserResponse.class)
                .toFuture();
    }

    /**
     * Fetches GitHub user repositories.     
     * Field mapping is already done by the response model class using Jackson annotations.
     *
     * @param username GitHub username.
     * @return A CompletableFuture containing a list of GithubApiReposResponse.
     */
    public CompletableFuture<List<GithubApiReposResponse>> fetchUserRepositories(String username) {
        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToMono(GithubApiReposResponse[].class)
                .map(List::of) // Convert array to List
                .toFuture();
    }
}
