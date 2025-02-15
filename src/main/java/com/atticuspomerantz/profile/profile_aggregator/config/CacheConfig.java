package com.atticuspomerantz.profile.profile_aggregator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to load cache settings from properties.
 */
@Configuration
public class CacheConfig {
    
    @Value("${cache.ttl.minutes:5}") // Grab from application.properties, Default to 5 minutes if not set
    private int ttlMinutes;

    public long getTtlMillis() {
        return ttlMinutes * 60 * 1000L; // Convert minutes to milliseconds
    }
}