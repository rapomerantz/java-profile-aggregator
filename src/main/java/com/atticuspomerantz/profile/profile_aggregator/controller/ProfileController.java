package com.atticuspomerantz.profile.profile_aggregator.controller;

import org.springframework.web.bind.annotation.RestController;

import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserProfile;
import com.atticuspomerantz.profile.profile_aggregator.service.ProfileService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * REST controller for retrieving GitHub user profiles.
 */
@RestController
@RequestMapping("/profile")
public class ProfileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);    
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Handles requests to /profile or /profile/ when no username is provided.
     */
    @GetMapping({ "", "/" })
    public ResponseEntity<String> handleMissingUsername() {
        LOGGER.warn("Received request with missing username.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required.");
    }

    /**
     * Retrieves the GitHub profile for a given username.
     * 
     * GlobalExceptionHandler will handle any exceptions thrown by the service.
     *
     * @param username GitHub username.
     * @return A ResponseEntity containing the GitHubUserProfile.
     */
    @GetMapping("/{username}")
    public ResponseEntity<GithubUserProfile> getProfile(@PathVariable String username) {
        if (username == null || username.trim().isEmpty()) {
            LOGGER.warn("Received request with missing or empty username.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        LOGGER.info("Received request for GitHub profile: {}", username);
        GithubUserProfile profile = profileService.getGithubProfile(username);
        return ResponseEntity.ok(profile);
    }
}
