package com.atticuspomerantz.profile.profile_aggregator.controller;

import org.springframework.web.bind.annotation.RestController;

import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserProfile;
import com.atticuspomerantz.profile.profile_aggregator.service.ProfileService;

import jakarta.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * Retrieves the GitHub profile for a given username.
     * 
     * GlobalExceptionHandler will handle any exceptions thrown by the service.
     *
     * @param username GitHub username.
     * @return A ResponseEntity containing the GitHubUserProfile.
     */
    @GetMapping("/{username}")
    public ResponseEntity<GithubUserProfile> getProfile(@PathVariable String username) {
        LOGGER.info("Received request for GitHub profile: {}", username);
        GithubUserProfile profile = profileService.getGithubProfile(username);
        return ResponseEntity.ok(profile);
    }
}
