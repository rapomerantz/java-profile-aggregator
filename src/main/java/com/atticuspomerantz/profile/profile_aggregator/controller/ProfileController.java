package com.atticuspomerantz.profile.profile_aggregator.controller;

import org.springframework.web.bind.annotation.RestController;

import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserProfile;
import com.atticuspomerantz.profile.profile_aggregator.service.ProfileService;

import java.util.logging.Logger;

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
    private static final Logger LOGGER = Logger.getLogger(ProfileController.class.getName());
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Retrieves the GitHub profile for a given username.
     *
     * @param username GitHub username.
     * @return A ResponseEntity containing the GitHubUserProfile or an appropriate error response.
     */
    @GetMapping("/{username}")
    public ResponseEntity<GithubUserProfile> getProfile(@PathVariable String username) {        
        try {
            GithubUserProfile profile = profileService.getGithubProfile(username);

            if (profile == null) {
                LOGGER.info("GitHub profile not found for: " + username);
                return ResponseEntity.notFound().build(); // Returns 404 NOT FOUND
            }

            return ResponseEntity.ok(profile);
        } catch (Exception ex) {
            LOGGER.info("Error retrieving GitHub profile for " + username + ": " + ex.getMessage());
            return ResponseEntity.internalServerError().build(); // Returns HTTP 500
        }
    }
}
