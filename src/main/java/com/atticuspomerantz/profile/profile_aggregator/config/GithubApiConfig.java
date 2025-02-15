package com.atticuspomerantz.profile.profile_aggregator.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to load GitHub API settings.
 */
@Configuration
public class GithubApiConfig {

    @Value("${github.api.timeout.seconds:5}") // Default to 5 seconds if not set
    private int timeoutSeconds;

    public Duration getTimeoutDuration() {
        return Duration.ofSeconds(timeoutSeconds);
    }
}