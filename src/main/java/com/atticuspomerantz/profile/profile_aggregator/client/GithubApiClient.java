package com.atticuspomerantz.profile.profile_aggregator.client;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiResponse;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserProfile;
import com.atticuspomerantz.profile.profile_aggregator.util.ApiConstants;


@Service
public class GithubApiClient {

    private final WebClient webClient;

    public GithubApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(ApiConstants.GITHUB_BASE_URL).build();
    }

    public CompletableFuture<GithubUserProfile> getProfile(String username) {
        return this.webClient.get()
            .uri("/users/{username}", username)
            .retrieve()
            .bodyToMono(GithubApiResponse.class)
            .map(this::mapToGithubUserProfile)
            .toFuture();
    }

    /* 
     * handling mapping in the client
     */
    private GithubUserProfile mapToGithubUserProfile(GithubApiResponse response) {
        return GithubUserProfile.builder()
            .user_name(response.getLogin())
            .email(response.getEmail())
            .display_name(response.getName())
            .avatar(response.getAvatar_url())
            .geo_location(response.getLocation())
            .url(response.getHtml_url())
            .created_at(response.getCreated_at())
            .build();
    }
}
